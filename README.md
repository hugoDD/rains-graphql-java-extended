# rains-graphql-java-extended
rains-graphql-java-extended 基于[graphql-java](https://github.com/graphql-java/graphql-java). [springboot],为了完善graphql方案进行的扩展。
主要是自定义类型和自定义指令的扩展。

[![Build Status](https://travis-ci.org/donbeave/graphql-java-datetime.svg?branch=master)](https://travis-ci.org/donbeave/graphql-java-datetime)
[![Latest Dev Build](https://api.bintray.com/packages/donbeave/maven/graphql-java-datetime/images/download.svg)](https://bintray.com/donbeave/maven/graphql-java-datetime/_latestVersion)

# 概要
 - `自定义类型`
   - 时间类型
     - **java.util.Date**, **java.time.LocalDate**, **java.time.LocalDateTime**
   - 对象（树)类型
 - `自定义指令Directive`
   - 验证指令
     - @Size 验证长度，集合大小等
     - @Expression el表达式
     - @AssertFalse 验证为false为合法
     - @AssertTrue 验证为true为合法
     - @DecimalMax 验证最大值
     - @DecimalMin 验证最小值
     - @Digits 验证数字格式
     - @Max 验证最大值
     - @Mix 验证最小值
     - @Negative验证负数
     - @NegativeOrZero 验证负数或者0
     - @NotBlank 非空
     - @NotEmpty 非空且值非空
     - @Pattern 验证表达式是否匹配
     - @Positive 正数
     - @PositiveOrZero 正数或者0
     - @Range 范围
     
     
## 详细
 ##### 时间类型
 **java.util.Date**, **java.time.LocalDate**, **java.time.LocalDateTime**

| Format                       | JSON String              |
|:-----------------------------|:-------------------------|
| yyyy-MM-dd HH:MM:ss.SSS      | 2017-07-09 13:14:45.94  |

**java.time.LocalTime**

| Format       | JSON String  |
|:-------------|:-------------|
| HH:MM:ss.SSS | 17:59:59.129 |
| HH:MM:ss     | 17:59:59     |
| HH:MM        | 17:59        |

##### 对象（树)类型
```graphql
scalar TreeNode
type Tree {
    id: ID
    parentId: ID
    name: String
    children: TreeNode

}
```

#####  验证
```graphql
input Application {
    name : String @Size( max : 100)
}

extend type  Query {
    hired (applications : [Application!] @Size(max : 3 )) : Boolean
}
```


# 使用

## Spring Boot

Add `graphql-extended-spring-boot-starter` starter to your project first.

### Installation

#### Maven

Add folowing to your `pom.xml`:

```xml
<dependency>
  <groupId>com.rains.graphql</groupId>
  <artifactId>graphql-extended-spring-boot-starter</artifactId>
  <version>2.10.5</version>
</dependency>
```

#### Gradle

Add folowing to your `build.gradle`:

```groovy
compile 'com.rains.graphql:graphql-extended-spring-boot-starter:2.10.5'
```

### Scalars

Add these scalars to your `.graphqls` schema file:

```graphql
# java.util.Date implementation
scalar Date

# java.time.LocalDate implementation
scalar LocalDate

# java.time.LocalDateTime implementation
scalar LocalDateTime

# java.time.LocalTime implementation
scalar LocalTime

# javal.time.OffsetDateTime implementation
scalar OffsetDateTime 

# 树children类型
scalar TreeNode

directive @Size(min : Int = 0, max : Int = 2147483647, message : String = "graphql.validation.Size.message")
on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION
```

您可以通过简单地将以下属性添加到application.yaml中来重命名scalars:

```yaml
graphql:
  datetime:
    scalars:
      date:
        scalarName: MyDate
        pattern: yyyy年MM月dd日 HH小时mm分ss秒
      localDate:
        scalarName: MyLocalDate
```


### 验证指令使用样例

### @AssertFalse

The boolean value must be false.

- Example : `updateDriver( isDrunk : Boolean @AssertFalse) : DriverDetails`

- Applies to : `Boolean`

- SDL : `directive @AssertFalse(message : String = "graphql.validation.AssertFalse.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.AssertFalse.message`


### @AssertTrue

The boolean value must be true.

- Example : `driveCar( hasLicence : Boolean @AssertTrue) : DriverDetails`

- Applies to : `Boolean`

- SDL : `directive @AssertTrue(message : String = "graphql.validation.AssertTrue.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.AssertTrue.message`


### @DecimalMax

The element must be a number whose value must be less than or equal to the specified maximum.

- Example : `driveCar( bloodAlcoholLevel : Float @DecimalMax(value : "0.05") : DriverDetails`

- Applies to : `String`, `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @DecimalMax(value : String!, inclusive : Boolean! = true, message : String = "graphql.validation.DecimalMax.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.DecimalMax.message`


### @DecimalMin

The element must be a number whose value must be greater than or equal to the specified minimum.

- Example : `driveCar( carHorsePower : Float @DecimalMin(value : "300.50") : DriverDetails`

- Applies to : `String`, `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @DecimalMin(value : String!, inclusive : Boolean! = true, message : String = "graphql.validation.DecimalMin.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.DecimalMin.message`


### @Digits

The element must be a number inside the specified `integer` and `fraction` range.

- Example : `buyCar( carCost : Float @Digits(integer : 5, fraction : 2) : DriverDetails`

- Applies to : `String`, `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @Digits(integer : Int!, fraction : Int!, message : String = "graphql.validation.Digits.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Digits.message`


### @Expression

The provided expression must evaluate to true.  The expression language is <a href="https://javaee.github.io/tutorial/jsf-el001.html">Java EL</a> and expressions MUST resolve to a boolean value, ie. it is valid or not.

- Example : `drivers( first : Int, after : String!, last : Int, before : String) 
 : DriverConnection @Expression(value : "${args.containsOneOf('first','last') }"`

- Applies to : `All Types and Scalars`

- SDL : `directive @Expression(value : String!, message : String = "graphql.validation.Expression.message") on FIELD_DEFINITION | ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Expression.message`


### @Max

The element must be a number whose value must be less than or equal to the specified maximum.

- Example : `driveCar( horsePower : Float @Max(value : 1000) : DriverDetails`

- Applies to : `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @Max(value : Int! = 2147483647, message : String = "graphql.validation.Max.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Max.message`


### @Min

The element must be a number whose value must be greater than or equal to the specified minimum.

- Example : `driveCar( age : Int @Min(value : 18) : DriverDetails`

- Applies to : `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @Min(value : Int! = 0, message : String = "graphql.validation.Min.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Min.message`


### @Negative

The element must be a negative number.

- Example : `driveCar( lostLicencePoints : Int @Negative) : DriverDetails`

- Applies to : `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @Negative(message : String = "graphql.validation.Negative.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Negative.message`


### @NegativeOrZero

The element must be a negative number or zero.

- Example : `driveCar( lostLicencePoints : Int @NegativeOrZero) : DriverDetails`

- Applies to : `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @NegativeOrZero(message : String = "graphql.validation.NegativeOrZero.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.NegativeOrZero.message`


### @NotBlank

The String must contain at least one non-whitespace character, according to Java's Character.isWhitespace().

- Example : `updateAccident( accidentNotes : String @NotBlank) : DriverDetails`

- Applies to : `String`

- SDL : `directive @NotBlank(message : String = "graphql.validation.NotBlank.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.NotBlank.message`


### @NotEmpty

The element must have a non zero size.

- Example : `updateAccident( accidentNotes : [Notes]! @NotEmpty) : DriverDetails`

- Applies to : `String`, `Lists`, `Input Objects`

- SDL : `directive @NotEmpty(message : String = "graphql.validation.NotEmpty.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.NotEmpty.message`


### @Pattern

The String must match the specified regular expression, which follows the Java regular expression conventions.

- Example : `updateDriver( licencePlate : String @Patttern(regex : "[A-Z][A-Z][A-Z]-[0-9][0-9][0-9]") : DriverDetails`

- Applies to : `String`

- SDL : `directive @Pattern(regexp : String! =".*", message : String = "graphql.validation.Pattern.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Pattern.message`


### @Positive

The element must be a positive number.

- Example : `driver( licencePoints : Int @Positive) : DriverDetails`

- Applies to : `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @Positive(message : String = "graphql.validation.Positive.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Positive.message`


### @PositiveOrZero

The element must be a positive number or zero.

- Example : `driver( licencePoints : Int @PositiveOrZero) : DriverDetails`

- Applies to : `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @PositiveOrZero(message : String = "graphql.validation.PositiveOrZero.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.PositiveOrZero.message`


### @Range

The element range must be between the specified `min` and `max` boundaries (inclusive).  It accepts numbers and strings that represent numerical values.

- Example : `driver( milesTravelled : Int @Range( min : 1000, max : 100000)) : DriverDetails`

- Applies to : `String`, `Byte`, `Short`, `Int`, `Long`, `BigDecimal`, `BigInteger`, `Float`

- SDL : `directive @Range(min : Int = 0, max : Int = 2147483647, message : String = "graphql.validation.Range.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Range.message`


### @Size

The element size must be between the specified `min` and `max` boundaries (inclusive).

- Example : `updateDrivingNotes( drivingNote : String @Size( min : 1000, max : 100000)) : DriverDetails`

- Applies to : `String`, `Lists`, `Input Objects`

- SDL : `directive @Size(min : Int = 0, max : Int = 2147483647, message : String = "graphql.validation.Size.message") on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION`

- Message : `graphql.validation.Size.message`



## Bugs

To report any bug, please use the project [Issues](https://github.com/hugoDD/rains-graphql-java-extended/issues/new) section on GitHub.


## License

Copyright © 2018-2019 [hugoDD](https://github.com/hugoDD). All rights reserved.

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.
