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

- **返回值：**无

- **描述：**根据函数名调用**本词库**中的函数，事件类型需要为[函数]

  > 添加初始化对象会导致被调函数无法访问到主调函数的变量，如下方示例所示。

- **示例：**

  ```text
  #Seiko词库V2
  
  ...code
  A<-1
  $调用 函数1$
  -----\n
  B<-{"A": 2}
  $调用 函数1 {B}$
  -----\n
  
  [函数]函数1
  {A}\n
  b\n
  c
  
  //1
  //b
  //c
  //----- 上面的调用中函数1访问到了变量，而下面的调用不然。
  //2
  //b
  //c
  -----
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

## 4. 读写类

### 4.1 读

- **指令：**`$读 文件路径 参数名(可选) 默认值(可选)$`

- **返回值：**若不填写参数名和默认值，返回整个文件的内容。反之返回参数名对应的值

- **描述：**读取一个文件

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
  
  