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

### 4.1 读

- **指令：**`$读 文件路径 参数名(可选) 默认值(可选)$`

- **返回值：**若不填写参数名和默认值，返回整个文件的内容。反之返回参数名对应的值

- **描述：**读取一个文件，若存在参数名和默认值的情况下读取的文件非JSON文件会报错

- **示例：**

  ```text
  ...code
  A<-$读 A.txt$
  B<-$读 A.txt key$
  ```


### 4.2 写

- **指令：**`$写 文件路径 键 值(可选)$`

- **返回值：**无

- **描述：**将键值对写入到文件中。若不填写值参数则会删除键

- **示例：**

  ```text
  ...code
  $写 A.txt key 1$
  ```


### 4.3 写对象

- **指令：**`$写对象 文件路径 {覆写对象(可选)}$`

- **返回值：**无

- **描述：**将覆写对象的内容覆盖到文件中。若覆写对象为空则删除这个文件

- **示例：**

  ```text
  $写 data/A.txt A 1$
  $写 data/A.txt B 2$
  $写 data/A.txt C 3$
  A<-$读 data/A.txt$
  A的内容:{A}\n
  B<-$读 data/A.txt C$
  C<-$读 data/A.txt D 0$
  C和D:{B}---{C}\n
  $写对象 data/A.txt {"114":514}$
  K<-$读 data/A.txt$
  重新写入后的A.txt:{K}
  //输出:
  //A的内容:{"114":514,"A":1,"B":2,"C":3}
  //C和D:3---0
  ```
  
  