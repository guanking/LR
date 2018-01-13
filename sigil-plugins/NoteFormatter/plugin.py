#!/usr/bin/env python
# -*- coding: utf-8 -*-
# vim:ts=4:sw=4:softtabstop=4:smarttab:expandtab

from __future__ import unicode_literals, division, absolute_import, print_function

import sys
import copy
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
    with open(fpath, 'r', encoding='utf-8') as file_obj:  # 注意编码
        content = ''
        for line in file_obj.readlines():
            content += line
        file_obj.close()
    return content


def deal_a_tag(orderContent, tempQp, orders):
    #print("orderContent : " + orderContent)
    tempQp.setContent(orderContent)
    ordersLen = len(orders)
    supBegin = False
    integrity = False
    obj = {}
    for text, prefix, name, tagtype, attrs in tempQp.parse_iter():
        if name == "sup":
            if tagtype == "begin":
                supBegin = True
            elif supBegin == True:
                integrity = True
                break
        elif name == "a" and tagtype == "begin":
            obj["href"] = attrs.get("href")
            obj["id"] = attrs.get("id")
    if integrity == False:
        return orderContent
    obj["order"] = str(ordersLen + 1)
    orders.append(obj)
    template = '<sup><a href="#note_{order}" id="noteBack_{order}">[{order}]</a></sup>'
    print(template.format(order=obj["order"]))
    return template.format(order=obj["order"])


def deal_p_tag(noteContent, tempQp, notes):
    #print("noteContent : " + noteContent)
    tempQp.setContent(noteContent)
    obj = {"note": ""}
    integrity = False
    aBegin = False
    for text, prefix, name, tagtype, attrs in tempQp.parse_iter():
        if name == "a":
            if tagtype == "begin":
                aBegin = True
                obj["href"] = attrs.get("href")
                obj["id"] = attrs.get("id")
            elif aBegin == True:
                integrity = True
        elif text is not None:
            obj["note"] += text
    beginIndex = obj["note"].find("]") + 1
    obj["note"] = obj["note"][beginIndex:]
    # print(obj)
    notes.append(obj)
    if integrity == False:
        return noteContent
    else:
        return ""


def create_note(orders, notes):
    # print(orders)
    # print(notes)
    template = '\t<p class="noteContent"><a href="#noteBack_{order}" id="note_{order}">[{order}]</a>{content}</p>\n'
    noteContent = '\t<p class="noteTitle">注释</p>\n'
    cnt = 0
    for order in orders:
        for note in notes:
            if order["href"] == "#" + note["id"] and "#" + order["id"] == note["href"]:
                cnt += 1
                curNote = template.format(
                    order=order["order"], content=note["note"])
                noteContent += curNote
                notes.remove(note)
                break
    # print(noteContent)

    if cnt == 0:
        print("\t\t该文件没有找到注释！")
        return ""
    else:
        print("\t\t共处理{cnt}个注释！".format(cnt=cnt))
        return noteContent


def run(bk):
    tempDir = tempfile.mkdtemp()
    # print(tempDir)#for test
    print('解压文件......')
    bk.copy_book_contents_to(tempDir)  # 解压到指定目录
    writeFile("application/epub+zip", "mimetype", tempDir, in_oebps=False)

    qp = bk.qp  # QuickXHTMLParser
    tempQp = copy.deepcopy(qp)
    for manifest_id, opf_href in bk.text_iter():  # 替换text文件中的a标签为img标签
        print('开始处理' + opf_href + '文件(manifest_id为：' + str(manifest_id) + ')......')
        # if(opf_href.find("Chapter4.xhtml") == -1):
        # continue
        content = bk.readfile(manifest_id)  # for test
        qp.setContent(content)
        newContent = ""
        orderContent = ""
        noteContent = ""
        orders = []
        notes = []
        orderBegin = False
        noteBegin = False
        for text, prefix, name, tagtype, attrs in qp.parse_iter():
            if name is None or tagtype is None:
                curContent = text
            else:
                curContent = qp.tag_info_to_xml(name, tagtype, attrs)

            if name == "a" and noteBegin == False:
                if tagtype == "begin":
                    orderBegin = True
                    orderContent = curContent
                elif tagtype == "end" and orderBegin == True:
                    orderBegin = False
                    orderContent += curContent
                    newContent += deal_a_tag(orderContent, tempQp, orders)
                else:
                    newContent += curContent
            elif name == "p":
                if tagtype == "begin" and attrs.get("class", "").find("noteContent") != -1:
                    noteBegin = True
                    noteContent = curContent
                    pass
                elif tagtype == "end" and noteBegin == True:
                    noteBegin = False
                    noteContent += curContent
                    newContent += deal_p_tag(noteContent, tempQp, notes)
                else:
                    newContent += curContent
            elif name == "body" and tagtype == "end":
                newContent += create_note(orders, notes)
            else:
                if noteBegin == True or orderBegin == True:
                    if noteBegin == True:
                        noteContent += curContent
                    if orderBegin == True:
                        orderContent += curContent
                else:
                    newContent += curContent
        writeFile(newContent, opf_href, tempDir)
        # break
    # return 0
    return save_result(tempDir)


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


def save_result(tempDir):  # 询问放置修改后epub文件的位置
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
    localRoot.quit()
    if not fpath:
        print("用户取消了保存修改后的epub文件")
    else:
        epub_zip_up_book_contents(tempDir, fpath)
        print("文件转化完成")
    shutil.rmtree(tempDir)  # 删除临时文件
    return 0


def main():
    print("I reached main when I should not have\n")
    return -1


if __name__ == "__main__":
    sys.exit(main())
