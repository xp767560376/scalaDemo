package test.service

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import test.pojo._
import wangzx.scala_commons.sql._

object Demo2 {
  val dataSource = {

    val ds = new MysqlDataSource
    ds.setURL(s"jdbc:mysql://127.0.0.1/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull")
    ds.setUser("root")
    ds.setPassword("123")
    ds
  }

  //各班语文成绩排名
  def chinese(): List[StudentChinese] ={
    dataSource.rows[StudentChinese](sql"SELECT c.name clazz ,s.name name ,s.chinese chinese FROM scala_student s,scala_clazz c WHERE s.c_id = c.id ORDER BY c.name DESC,chinese DESC")
  }

  //各班数学成绩排名
  def math(): List[StudentMath] ={
    dataSource.rows[StudentMath](sql"SELECT c.name clazz ,s.name name ,s.math math FROM scala_student s,scala_clazz c WHERE s.c_id = c.id ORDER BY c.name DESC,math DESC")
  }

  //各班英语成绩排名
  def english(): List[StudentEnglish] = {
    dataSource.rows[StudentEnglish](sql"SELECT c.name clazz ,s.name name ,s.english english FROM scala_student s,scala_clazz c WHERE s.c_id = c.id ORDER BY c.name DESC,english DESC")
  }

  //按班级排总分
  def sum(): List[StudentClazzSum] ={
    dataSource.rows[StudentClazzSum](sql"SELECT c.name clazz ,s.name NAME ,(s.math+s.chinese+s.english)total FROM scala_student s,scala_clazz c WHERE s.c_id = c.id ORDER BY c.name DESC,total DESC")
  }

  //查询所有班级
  def clazz(): List[String] = {
    dataSource.rows[String](sql"SELECT NAME FROM scala_clazz")
  }

  //所有学员平均分
  def avg(): List[StudentAvg] = {
    dataSource.rows[StudentAvg](sql"SELECT c.name clazz, s.name NAME,s.sex gender ,(chinese+math+english)/3 AVG FROM scala_student s,scala_clazz c WHERE s.c_id=c.id ORDER BY c.name asc,AVG desc")
  }

  //学生总人数
  def count(): Option[Int] = {
    dataSource.row[Int](sql"SELECT COUNT(1) FROM scala_student")
  }

  //所有语文成绩
  def chineseScore(): List[Int] = {
    dataSource.rows[Int](sql"SELECT chinese FROM scala_student ORDER BY chinese desc")
  }

  //所有数学成绩
  def mathScore(): List[Int] = {
    dataSource.rows[Int](sql"SELECT math FROM scala_student ORDER BY math desc")
  }

  //所有英语成绩
  def englishScore(): List[Int] = {
    dataSource.rows[Int](sql"SELECT english FROM scala_student ORDER BY english desc")
  }

  //按年级字段排总分
  def gradeScore(): List[StudentGradeSum] = {
    dataSource.rows[StudentGradeSum](sql"SELECT g.name grade,s.name NAME,s.sex gender,(chinese+math+english)score FROM scala_student s,scala_clazz c,scala_grade g WHERE s.c_id=c.id AND c.g_id=g.id ORDER BY g.name ASC ,score desc")
  }

  //所有年级
  def grade(): List[String] = {
    dataSource.rows[String](sql"SELECT NAME FROM scala_grade")
  }

  //各科状元
  def numOne(): List[StudentNumOne] = {
    dataSource.rows[StudentNumOne](sql"SELECT c.name clazz,s.sex gender,s.name NAME,s.chinese chineseScore,s.math mathScore,s.english englishScore FROM (SELECT * FROM scala_student s WHERE s.chinese = (SELECT MAX(chinese) FROM scala_student) OR s.math = (SELECT MAX(math) FROM scala_student) OR s.english = (SELECT MAX(english) FROM scala_student)) s ,scala_clazz c WHERE s.c_id = c.id")
  }

  def main(args: Array[String]): Unit = {
    //各个班级各科前五名
    println("======================各个班级语文前五名===============================")
    for (clazz <- Demo2.clazz()){
     for( a <- Demo2.chinese().filter(x => x.clazz.contains(clazz)).take(5)){
       println(a.clazz+"语文排名"+a.name,a.chinese)
     }
    }
    println("======================各个班级数学前五名===============================")
    for (clazz <- Demo2.clazz()){
      for( a <- Demo2.math().filter(x => x.clazz.contains(clazz)).take(5)){
        println(a.clazz+"数学排名"+a.name,a.math)
      }
    }
    println("======================各个班级英语前五名===============================")
    for (clazz <- Demo2.clazz()){
      for( a <- Demo2.english().filter(x => x.clazz.contains(clazz)).take(5)){
        println(a.clazz+"英语排名"+a.name,a.english)
      }
    }
    println("======================各个班级总分前五名===============================")
    for (clazz <- Demo2.clazz()){
      for( a <- Demo2.sum().filter(x => x.clazz.contains(clazz)).take(5)){
        println(a.clazz+"总分排名"+a.name,a.total)
      }
    }

    println("=========================学生平均分============================")
    //学生平均分
    for( avg<- Demo2.avg()) {
      println(avg.clazz+"   姓名："+avg.name+",  性别："+avg.gender+",  平均分："+avg.avg)
    }

    println("========================语文成绩等级=============================")
    //语文成绩等级
    val chineseGood = Demo2.chineseScore().filter(score => score.>=(90)).length
    val score = Demo2.chineseScore().length
    println("语文成绩优秀："+chineseGood*100/score+"%")

    val chineseNice = Demo2.chineseScore().filter(score => score.>=(75)&score.<(90)).length
    println("语文成绩良好："+chineseNice*100/score+"%")

    val chinesePass = Demo2.chineseScore().filter(score => score.>=(60)&score.<(75)).length
    println("语文成绩及格："+chinesePass*100/score+"%")

    val chineseFail = Demo2.chineseScore().filter(score => score.<(60)).length
    println("语文成绩不及格："+chineseFail*100/score+"%")

    println("========================数学成绩等级=============================")
    //数学成绩等级
    val mathGood = Demo2.mathScore().filter(score =>score.>=(90)).length
    println("数学成绩优秀："+mathGood*100/score+"%")

    val mathNice = Demo2.mathScore().filter(score =>score.>=(75)&score.<(90)).length
    println("数学成绩良好："+mathNice*100/score+"%")

    val mathPass = Demo2.mathScore().filter(score => score.>=(60)&score.<(75)).length
    println("数学成绩及格："+mathPass*100/score+"%")

    val mathFail = Demo2.mathScore().filter(score => score.<(60)).length
    println("数学成绩不及格："+mathFail*100/score+"%")

    println("========================英语成绩等级=============================")
    //英语成绩等级
    val englishGood = Demo2.englishScore().filter(score =>score.>=(90)).length
    println("英语成绩优秀："+englishGood*100/score+"%")

    val englishNice = Demo2.englishScore().filter(score =>score.>=(75)&score.<(90)).length
    println("英语成绩良好："+englishNice*100/score+"%")

    val englishPass = Demo2.englishScore().filter(score => score.>=(60)&score.<(75)).length
    println("英语成绩及格："+englishPass*100/score+"%")

    val englishFail = Demo2.englishScore().filter(score => score.<(60)).length
    println("英语成绩不及格："+englishFail*100/score+"%")

    println("==========================各年级总分前10名===========================")
    //计算各年级总分前10名
    for(grade <- Demo2.grade()){
     for (gradeTop10 <- Demo2.gradeScore().filter(x =>x.grade.contains(grade)).take(10)){
       println(gradeTop10.grade+"; "+gradeTop10.gender+"; "+gradeTop10.name+"; "+gradeTop10.score)
     }
    }

    println("==========================各科男女状元===========================")
    //计算各科男女状元
    for ( numOne <- Demo2.numOne().filter(score => score.chineseScore.==(100))){
      println("语文状元："+numOne.clazz+"  "+numOne.name+"  "+numOne.chineseScore)
    }
    for ( numOne <- Demo2.numOne().filter(score => score.mathScore.==(100))){
      println("数学状元："+numOne.clazz+"  "+numOne.name+"  "+numOne.mathScore)
    }
    for ( numOne <- Demo2.numOne().filter(score => score.englishScore.==(100))){
      println("英语状元："+numOne.clazz+"  "+numOne.name+"  "+numOne.englishScore)
    }
  }
}
