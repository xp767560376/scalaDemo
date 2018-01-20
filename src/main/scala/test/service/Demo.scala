package test.service

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import wangzx.scala_commons.sql._
import test.pojo.{Stu1, StudentMath}

object Demo  {
    val dataSource = {

      val ds = new MysqlDataSource
      ds.setURL(s"jdbc:mysql://127.0.0.1/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull")
      ds.setUser("root")
      ds.setPassword("123")
      ds
    }

    def foo(): List[Stu1] ={
      dataSource.rows[Stu1](sql"SELECT * FROM student")
    }

  def main(args: Array[String]): Unit = {
    for (elem <- Demo.foo) {
      println(elem.name)
    }
  }
}
