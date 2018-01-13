import hashlib
import datetime
now = datetime.datetime.now()
m = hashlib.md5();
str = u'myt'
str = str.encode("utf-8")
m = hashlib.md5();
m.update(str)
temp = m.hexdigest()
str = now.strftime('%Y-%m-%d %H:%M:%S')  
str = str.encode("utf-8")
m.update(str)
print(temp + m.hexdigest())
print("Conpile Finish!");