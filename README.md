#TinyPNG_JavaClient

一个简陋的Swing版TinyPNG客户端。

[TinyPNG](https://tinypng.com/)是一个高效的png/jpg图片在线压缩网站。

*jpeg扩展名的图片不能识别，请转为jpg格式（貌似两者之间有一点点不同）*

功能：
- 能够选择一个文件夹下的所有图片，也可以拖拽一组图片进行添加。
- 自动完成图片的上传和下载压缩后图片。

配置TinyPNG Key& 输出目录

> 你会在jar文件所在目录下有tiny.properties这个配置文件，修改相应的参数即可。

default ``tiny.properties``
```txt
#Fri May 12 14:27:37 CST 2017
outDir=jar文件所在目录\out\
key= tiny key
```
