# Base_Command

这里存储了所有基本的指令集。这些指令是任何一个SeikoDIC都可以使用的。



# 指令列表

## 0. 示例

### 0.0 示例

- **指令：**`$$`

- **返回值：**

- **描述：**

- **示例：**

  ```text
  1
  ```
  
  

## 1. 集合类

### 1.1 集合克隆

- **指令**：`$集合克隆 {集合对象}$`

- **返回值：**新的集合对象，当传入对象非集合时返回null

- **描述：**复制一个集合。

- **示例：**

  ```text
  a<-[1,2,3,4,5]
  b<-$集合克隆 {a}$
  ```

### 1.2 集合长

- **指令：**`$集合长 {集合对象}$`

- **返回值：**一个数字，代表集合的长度。当传入对象非集合时返回null

- **描述：**获取这个集合的长度

- **示例：**

  ```text
  a<-[1,2,3,4,5]
  //5
  b<-$集合长 {a}$
  ```

### 1.3 集合检验

- **指令：**`$集合检验 {集合对象} {检验对象}$`

- **返回值：**固定值true/false，当{集合对象}非集合对象时抛错。

- **描述：**检查列表值是否存在 或 检查键值对键是否存在。

- **示例：**

  ```text
  a<-[1,2,3,4,5]
  //false
  b<-$集合检验 {a} 6$
  ```

  

## 2. 设置类

### 2.1 设置键

- **指令：**`$设置键$`

- **返回值：**一个列表，里面存放所有设置项的键

- **描述：**获取词库所有设置的键

- **示例：**

  ```text
  #Seiko词库V2
  #设置A: 114514
  #设置B: 114514
  #设置C: 114514
  #设置D: 114514
  
  ...code...
  A<-$设置键$
  //["设置A","设置B","设置C","设置D"]
  {A}
  ```

### 2.2 取设置

- **指令：**`$取设置 键 默认值(可选)$`

- **返回值：**一个字符串，当默认值不填写且设置不存在此键时返回null

- **描述：**获取词库设置中的内容

- **示例：**

  ```text
  #Seiko词库V2
  #设置A: 114514
  
  ...code...
  //114514
  A<-$设置 设置A$
  //null
  B<-$设置 设置B$
  //def
  C<-$设置 设置B def$
  ```

## 3. 调用类

### 3.1 调用

- **指令：**`$调用 函数名 初始化对象(可选)$`

- **返回值：**当不填写初始化对象时无返回值，当填写初始化对象时返回函数的变量表

- **描述：**根据函数名调用**本词库**中的函数，事件类型需要为[函数]

  > 当添加初始化对象时：
  >
  > 被调函数无法访问到主调函数的变量，同时被调函数的变量表将会返回到主调函数中。

- **示例：**

  ```text
  [控制台]调用测试
  A<-1
  //这个调用的变量表继承被调函数
  $调用 函数1$
  //这个调用的变量表不继承被调函数，因此被调函数执行后会将被调函数的变量表加以返回
  //预期输出为:
  //A的值为:1
  //A的值为:2
  //函数返回值为:{A=2, M=1, N=2}
  B<-$调用 函数1 {"A":2}$
  函数返回值为:{B}
  
  [函数]函数1
  A的值为:{A}\n
  M<-1
  N<-2
  ```

### 3.2 跨词库调用

- **指令：**`$跨词库调用 词库名 函数名 上下文对象(可选)$`

- **返回值：**无

- **描述：**根据函数名调用**其他词库**中的函数，事件类型需要为[函数]

  > 1. 单词库调用此函数会抛错
  > 2. 变量覆盖规则和**3.1**一致。

- **示例：**

  ```text
  #Seiko词库V2
  
  
  ...code
  $跨词库调用 子词库1.txt 子函数1 {"A":4}$
  //4
  ```

  ```text
  #Seiko词库V2
  
  [函数]子函数1
  {A}
  ```

### 3.3 异步调用

- **指令：**`$异步调用 {选项}$`

- **返回值：**无

- **描述**：**不阻塞地**调用**本词库**中的函数，事件类型需要为[函数]

  > 选项是一个集合，可选值如下:(加粗的选项为必选)
  >
  > | 键          | 值类型 | 说明                                                       |
  > | ----------- | ------ | ---------------------------------------------------------- |
  > | **command** | 字符串 | 本次异步调用要调用的函数，下文成子调函数。                 |
  > | args        | 集合   | 本次异步调用要传的参数。若不填写则**克隆**被调函数的变量集 |
  > | call        | 字符串 | 异步函数的回调函数，**变量集**与子调函数中的变量集相同     |

- **示例**：

  ```text
  [群]异步测试
  B<-0
  A<-{"command":"函数1"}
  $异步调用 {A}$
  A<-{"command":"函数1","args":{"A":1,"B":2000}}
  $异步调用 {A}$
  B<-3000
  A<-{"command":"函数1","call":"回调函数"}
  $异步调用 {A}$
  A<-{"command":"函数1","args":{"A":2,"B":4000},"call":"回调函数"}
  $异步调用 {A}$
  
  [函数]函数1
  //直接填写{B}会报错，因此需要这么写以设定B的默认值
  试错:
   K<-{B}
  捕获:e
   B<-1000
  $延时 {B}$
  我是函数1,A的值为{A}
  
  
  [函数]回调函数
  回调函数被执行，上一个作用域为{A}
  ```

  运行结果如下:

  ```text
  我是函数1,A的值为{"command":"函数1"}
  我是函数1,A的值为1
  我是函数1,A的值为{"command":"函数1","call":"回调函数"}
  回调函数被执行，上一个作用域为{"command":"函数1","call":"回调函数"}
  --------------
  我是函数1,A的值为2
  回调函数被执行，上一个作用域为2
  --------------
  ```

### 3.4 延时

- **指令：**`$延时 n$`

- **返回值：**无

- **描述：**将指令所在的函数暂停n毫秒

  > 1. 调用前会发送消息。

- **示例：**

  ```text
  #Seiko词库V2
  
  
  ...code
  a
  $延时 1000$
  b
  ```


## 4. 读写类

### 4.1 读对象

- **指令：**`$读对象 文件路径 默认值 自动保存(可选)$`

- **返回值：**一个集合对象

- **描述：**读入一个JSON文件，并转换成集合对象

  若文件不存在则返回默认值。

  若自动保存的值为true，则对其中任何一个对象的更改都将自动保存(实验性)，否则需要手动调用`$写对象$`函数进行保存

- **示例：**

  ```text
  ...code
  A<-$读对象 A.txt$
  B<-$读对象 A.txt {}$
  C<-$读对象 B.txt {} true$
  {C.msg}<-true
  //对响应式对象的赋值会直接保存在本地
  ```

### 4.2 写对象

- **指令：**`$写对象 文件路径 {覆写对象}(可选)$`

- **返回值：**无

- **描述：**将覆写对象的内容覆盖到文件中。若不填写覆写对象则删除这个文件

- **示例：**

  ```text
  $写对象 A.txt$
  $写对象 B.txt {"status":1}$
  ```
  

### 4.3 读字节

- **指令：**`$读字节 文件路径$`

- **返回值：**集合

- **描述：**读取文件的二进制并转换成集合，若文件不存在则返回空集合

- **示例：**

  ```text
  $读字节 A.txt$
  ```
  

### 4.4 写字节

- **指令：**`$写字节 文件路径 {覆写集合}(可选)$$`

- **返回值：**无

- **描述：**将覆写集合的二进制覆盖到文件中。若不填写覆写集合则删除这个文件

- **示例：**

  ```text
   $写字节 A.txt$
   $写字节 B.txt [123,34,97,100,100,49,34,58,49,57,49,57,44,34,97,100,100,50,34,58,49,57,49,57,125]$
  ```

## 5. 生成类

### 5.1 随机数

- **指令：**`$随机数$ $随机数 {最大值}$ $随机数 {最小值} {最大值}$`

- **返回值：**数字

- **描述：**无参数返回0-1之间的小数，一个参数返回[0,最大值)之间的整数，两个参数返回[最小值,最大值)之间的整数

- **示例：**

  ```text
  A<-$随机数$
  B<-$随机数 10$
  C<-$随机数 100 1000$
  ```
  

### 5.2 随机uuid

- **指令：**`$UUID$`

- **返回值：**字符串

- **描述：**生成一个uuid。uuid是一个长度为32的字符串，可以保证在**同一台机器上**每时每刻生成的uuid是不同的

- **示例：**

  ```text
  $UUID$
  ```

## 5. 网络类

### 4.1 HTTP

- **指令：**`$HTTP 设置集合$`

- **返回值：**集合对象，描述见下：

- **描述：**

  设置集合可填写值如下(加粗为必选):

  | 键         | 描述                                                         | 默认值 |
  | ---------- | ------------------------------------------------------------ | ------ |
  | **url**    | 访问的url路径                                                |        |
  | method     | 请求方式，为GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE; | GET    |
  | header     | 请求头                                                       | {}     |
  | cookie     | 请求携带Cookie                                               | {}     |
  | bodyOrData | 此值为集合时按照集合格式填充数据，为字符串时则填充body       | {}     |
  | timeout    | 超时时间                                                     | 1000   |

  返回的集合对象如下：

  | 键      | 描述                 | 类型           |
  | ------- | -------------------- | -------------- |
  | code    | 请求码               | 数字           |
  | msg     | 请求码配套的请求信息 | 字符串         |
  | body    | 返回的内容           | 集合(字节数组) |
  | headers | 返回头               | 集合           |
  | cookie  | 返回cookie           | 集合           |

- **示例：**

  ```text
  options<-///
  {
      "url": "http://www.baidu.com",
      "method": "POST",
      "header": {
          "a": "1"
      },
      "cookie": {
          "b": "2"
      },
      "bodyOrData": "123"
  }
  ///
  A<-$HTTP {options}$
  {A}\n
  $延时 1000$
  {options.bodyOrData}<-///
  {
      "A":"1",
      "B":"2"
  }
  ///
  A<-$HTTP {options}$
  {A}
  ```

## 6. 字符串类

### 4.1 字符串创建

- **指令：**`$字符串创建 集合(字节数组) 编码(可选)$`

- **返回值：**字符串

- **描述：**读入一个集合，以其值为字节数组创建一个字符串

- **示例：**

  ```text
  A<-[-28, -67, -96, -26, -119, -128, -25, -125, -83, -25, -120, -79, -25, -102, -124, -17, -68, -116, -27, -80, -79, -26, -104, -81, -28, -67, -96, -25, -102, -124, -25, -108, -97, -26, -76, -69]
  A<-$字符串创建 {A}$
  ```

### 4.2 字符串分割

- **指令：**`$字符串分割 字符串 分割符$`

- **返回值：**字符串

- **描述：**将字符串以分隔符为界分割成子集合

- **示例：**

  ```text
  A<-我爱玩原神
  $字符串分割 {A} 玩$
  ```

### 4.3 转(大/小)写

- **指令：**`$转(大/小)写 字符串 分割符$`

- **返回值：**字符串

- **描述：**将字符串全部转为(大/小)写

- **示例：**

  ```text
  A<-$转大写 aaaaaa$
  {A}
  A<-$转小写 AAAAAA$
  {A}
  ```

### 4.4 关键词检测

- **指令：**`$关键词检测 字符串 子关键词$`

- **返回值：**固定值，为true或false

- **描述：**读入一个字符串，检测是否存在子关键词。

- **示例：**

  ```text
   A<-$关键词检测 我爱玩原神 原神$
   如果:{A}==true
    检测到原神关键词
   如果尾
    没有检测到原神关键词
  ```

### 4.5 替换

- **指令：**`$替换 待替换字符串 关键词 替换字符串$`

- **返回值：**字符串

- **描述：**将字符串中的关键词替换成替换字符串

- **示例：**

  ```text
  $替换 addbbbcccddd aaa eee$
  ```

### 4.6 正则匹配

- **指令：**`$正则 字符串 正则表达式$`

- **返回值：**集合

- **描述：**匹配正则表达式，将符合要求的子字符串制作成集合返回给词库。

- **示例：**

  ```text
  $替换 addbbbcccddd aaa eee$
  ```

