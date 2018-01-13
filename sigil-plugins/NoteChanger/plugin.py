#!/usr/bin/env python
# -*- coding: utf-8 -*-
# vim:ts=4:sw=4:softtabstop=4:smarttab:expandtab

from __future__ import unicode_literals, division, absolute_import, print_function

import sys
import os
import tempfile
import shutil
import re
from itertools import count
from _ast import Str

try:
    from urllib.parse import unquote
except ImportError:
    from urllib import unquote
from epub_utils import epub_zip_up_book_contents

PY2 = sys.version_info[0] == 2

if PY2:
    import Tkinter as tkinter
    import ttk as tkinter_ttk
    import Tkconstants as tkinter_constants
    import tkFileDialog as tkinter_filedialog
else:
    import tkinter
    import tkinter.ttk as tkinter_ttk
    import tkinter.constants as tkinter_constants
    import tkinter.filedialog as tkinter_filedialog
_USER_HOME = os.path.expanduser("~")


def getClassValues():
    for ele in sys.path:
        if 'NoteChanger' in ele:
            with open(ele + "/config.ini", "r") as configFile:
                values = configFile.readlines()
                configFile.close()
                return [value.strip() for value in values]
    return None


def getAllNotations(bk, noteClassList):
    """
    获取所有的注释,如果注释中有图片就返回false
    type:字典,key为指向该注释的超链接，value是一个字典。
    value:tags指向的是一个list，里面是包含p标签在内的所有标签，标签是接口中的标准结构；content指向的是注释内容
    """
    print('获取所有注释内容......')
    notations = {}  # href+id->content
    for manifest_id, opf_href in bk.text_iter():
        print('获取' + opf_href + '文件中的注释......')
        countNotation = 0
        content = bk.readfile(manifest_id)  # 'x_Section0003.xhtml' for test
        qp = bk.qp
        qp.setContent(content)
        tempContainer = []
        status = 0
        for text, prefix, name, tagtype, attrs in qp.parse_iter():
            # print([name,attrs,text,tagtype,status])
            # print([status is 0 , name=='p' , tagtype=='begin' , attrs is not None , attrs])
            if status is 0 and name == 'p' and tagtype == 'begin' and attrs is not None and 'class' in attrs:
                if noteClassList.count(attrs['class']) is 0:  # 判断是否为注释
                    continue
                tempContainer = [(text, prefix, name, tagtype, attrs)]
                status = 1
            elif status is 1 and name == 'p' and tagtype == 'end':
                tempContainer.append((text, prefix, name, tagtype, attrs))
                notecontent = ''
                temphref = None
                for ele in tempContainer:
                    if ele[2] == 'img':
                        print(opf_href + '文件的注释中存在图片')
                        print('位置：\n\t' + opf_href +
                              '》'.join(ele[1].split('.')))
                        print('各属性值：')
                        for key in ele[4].keys():
                            print('\n\t' + key + '="' + ele[4][key] + '"')
                        return False  # 如果有图片就返回false
                    if ele[2] == 'a' and ele[4] is not None and 'href' in ele[4] and 'id' in ele[4]:
                        temphref = '../' + opf_href + '#' + ele[4]['id']
                    elif temphref is not None and ele[0] is not None:
                        notecontent += ele[0].strip()
                if temphref is not None:
                    notations[temphref] = {
                        'tags': tempContainer.copy(), 'content': notecontent}
                    countNotation += 1
                status = 0
            elif status is 1:
                tempContainer.append((text, prefix, name, tagtype, attrs))
        print('\t' + opf_href + '文件共有注释:' + str(countNotation) + '条')
        # break for test
    return notations


def writeFile(data, href, temp_dir, in_oebps=True):
    """
     将data数据写入到temp_dir/OEBPS/href文件中，或者temp_dir/href 文件中
    :param data: 数据
    :type  data: str
    :param href:
    :type  href: str
    :param temp_dir:
    :type  temp_dir: str
    :param in_oebps: 如果为true就写入到temp_dir/OEBPS/href，否则写入到temp_dir/href
    :type  in_oebps: bool
    """
    destdir = ""
    filename = unquote(href)
    if "/" in href:
        destdir, filename = unquote(filename).split("/")
    fpath = os.path.join(temp_dir, "OEBPS", destdir, filename)
    if in_oebps:
        fpath = os.path.join(temp_dir, "OEBPS", href)
    else:
        fpath = os.path.join(temp_dir, href)
    with open(fpath, "wb") as file_obj:
        file_obj.write(data.encode("utf-8"))


def readFile(temp_dir, href):  # 读取oepbs目录下href指定的文件（utf-8编码）内容
    destdir, filename = unquote(href).split("/")
    fpath = os.path.join(temp_dir, "OEBPS", destdir, filename)
    content = None
    # print(temp_dir)
    # print(href)
    with open(fpath, 'r', encoding='utf-8') as file_obj:  # 注意编码
        content = ''
        for line in file_obj.readlines():
            content += line
        file_obj.close()
    # print(content)
    return content


def del_notations(qp, href, delnote, notes, noteClassList):
    """
    删除qp中所包含的内容中的注释,返回删除后的文本内容
    qp：htmlquickparser,里面的content是href所指定的文件内容
    href:当前文件的href
    delnote：要删除的注释
    notes：所有的注释
    noteClassList：判断是否为注释的class值
    """
    print('删除' + href + '文件中的注释......')
    content = ''
    tempContainer = []
    status = 0
    delnotescount = 0
    for text, prefix, name, tagtype, attrs in qp.parse_iter():
        # print([name,attrs,text,tagtype,status])#for test
        # print([status is 0 , name=='p' , tagtype=='begin' , attrs is not None , attrs])
        if status is 0 and name == 'p' and tagtype == 'begin' and attrs is not None and 'class' in attrs:
            if noteClassList.count(attrs['class']) is not 0:  # 判断是否为注释
                tempContainer = [(text, prefix, name, tagtype, attrs)]
                status = 1
                continue
        elif status is 1 and name == 'p' and tagtype == 'end':
            tempContainer.append((text, prefix, name, tagtype, attrs))
            temphref = None
            delcurrentnota = False
            containercontent = ''
            for ele in tempContainer:
                if ele[2] == 'a' and ele[4] is not None and 'href' in ele[4] and 'id' in ele[4]:
                    temphref = '../' + href + '#' + ele[4]['id']
                    if temphref in delnote:  # 删除注释
                        del delnote[delnote.index(temphref)]
                        delcurrentnota = True
                        delnotescount += 1  # 删除计数加一
                        print('\t删除id为' + str(ele[4]['id']) + '的注释')
                        break
                if ele[2] is None or ele[3] is None:
                    containercontent += ele[0]
                else:
                    containercontent += qp.tag_info_to_xml(
                        ele[2], ele[3], ele[4])
            if delcurrentnota == False:
                # print(containercontent)for test
                content += containercontent
            status = 0
            continue
        elif status is 1:
            tempContainer.append((text, prefix, name, tagtype, attrs))
            continue  # 一定要continue，避免只删除p标签
        if name is None or tagtype is None:
            content += text
        else:
            content += qp.tag_info_to_xml(name, tagtype, attrs)
    print('\t删除' + href + '文件中的注释完成，共：' + str(delnotescount) + '处')
    # print(content)#for test
    return content


def run(bk):
    """
    程序 入口
    """
    tempDir = tempfile.mkdtemp()
    # print(tempDir)#for test
    print('本次使用的判断注释的class值有：')
    classvalues = getClassValues()
    print(classvalues)
    print('解压文件......')
    bk.copy_book_contents_to(tempDir)  # 解压到指定目录
    writeFile("application/epub+zip", "mimetype", tempDir, in_oebps=False)
    print('解压文件完成，开始转化......')
    notations = getAllNotations(bk, classvalues)  # 所有注释
    if notations == False:
        shutil.rmtree(tempDir)
        return 1  # 注释中存在图片
    delnotations = []  # 所有需要被删除的注释的超链接
    # for key in iter(notations.keys()):print(key+':'+str(notations[key]['tags']))#输出获取到的注释
    for manifest_id, opf_href in bk.text_iter():  # 替换text文件中的a标签为img标签
        print('开始处理' + opf_href + '文件(manifest_id为：' + str(manifest_id) + ')......')
        content = bk.readfile(manifest_id)  # for test
        # print(content)#for test
        newcontent = ''
        qp = bk.qp
        qp.setContent(content)
        status = 0
        noteCount = 0
        noteContent = None
        for text, prefix, name, tagtype, attrs in qp.parse_iter():
            # print([name,tagtype,attrs])#for test
            if status is 0 and name == 'a' and tagtype == 'begin' and attrs is not None and 'href' in attrs:
                # print([name,tagtype,attrs])#for test
                attr_href = str(attrs['href'])
                if not attr_href.startswith('..'):
                    attr_href = '../' + opf_href + attr_href

                if attr_href in notations:
                    print('\thref为' + attr_href + ',id为' + str(attrs['id']))
                    noteContent = notations[attr_href]['content']
                    if delnotations.count(attr_href) is 0:
                        delnotations.append(attr_href)
                    status = 1
                    continue
            elif status is 1 and name == 'a' and tagtype == 'end':  # 替换标签
                newcontent += qp.tag_info_to_xml('img', 'single', {'alt': noteContent, 'class': 'dd-footnote',
                                                                   'src': '../Images/note.png'})
                # newcontent+=qp.tag_info_to_xml('img', 'end')
                noteCount += 1
                status = 0
                continue
            elif status is 1:
                continue
            if name is None or tagtype is None:
                newcontent += text
            else:
                newcontent += qp.tag_info_to_xml(name, tagtype, attrs)
        print('\t' + opf_href + '文件处理完毕,共修改：' + str(noteCount) + '处')
        # print(newcontent)#for test
        writeFile(newcontent, opf_href, tempDir)
        # return 0#for test
    # 删除所有需要删除的注释（有待调试）
    curdelnotationslen = len(delnotations)
    while len(delnotations) > 0:
        # print(delnotations)#for test
        href = delnotations[0].split('#')[0]
        href = href[3:]  # 去掉href前面的‘../’字符串
        delcontent = readFile(tempDir, href)  # 'Text/part0008.html')
        bk.qp.setContent(delcontent)
        content = del_notations(bk.qp, href, delnotations,
                                notations, classvalues)  # 删除href文件中的注释
        if len(delnotations) >= curdelnotationslen:  # 本次循环没有注释被删除
            print('文档' + href + '注释不合法，请检查文档：可能原因：1.删除过程中，文件被修改；2.存在注释跳转到注释的超链接等')
            print('注释的id为：' + delnotations[0].split('#')[1])
            shutil.rmtree(tempDir)  # 删除临时文件
            return 1
        curdelnotationslen = len(delnotations)
        writeFile(content, href, tempDir)
        # return 0#for test
    # 询问放置修改后epub文件的位置
    doctitle = "newepub"
    fname = cleanup_file_name(doctitle) + "_noteChanger.epub"
    localRoot = tkinter.Tk()
    localRoot.withdraw()
    fpath = tkinter_filedialog.asksaveasfilename(
        parent=localRoot,
        title="存储为 ...",
        initialfile=fname,
        initialdir=_USER_HOME,
        defaultextension=".epub"
    )
    # localRoot.destroy()
    localRoot.quit()
    if not fpath:
        print("用户取消了保存修改后的epub文件")
    else:
        epub_zip_up_book_contents(tempDir, fpath)
        print("文件转化完成")
    shutil.rmtree(tempDir)  # 删除临时文件
    return 0


def cleanup_file_name(name):
    import string
    _filename_sanitize = re.compile(r'[\xae\0\\|\?\*<":>\+/]')
    substitute = '_'
    one = ''.join(char for char in name if char in string.printable)
    one = _filename_sanitize.sub(substitute, one)
    one = re.sub(r'\s', '_', one).strip()
    one = re.sub(r'^\.+$', '_', one)
    one = one.replace('..', substitute)
    # Windows doesn't like path components that end with a period
    if one.endswith('.'):
        one = one[:-1] + substitute
    # Mac and Unix don't like file names that begin with a full stop
    if len(one) > 0 and one[0:1] == '.':
        one = substitute + one[1:]
    return one


def main():
    with open("./config.ini", "r") as configFile:
        values = configFile.readlines()
        print(values)
        configFile.close()
    print("I reached main when I should not have\n")
    return -1


if __name__ == "__main__":
    sys.exit(main())
