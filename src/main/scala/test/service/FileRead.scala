package test.service

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.io.Source


object FileRead extends App {
  //读取文件
  val source = Source.fromFile("E:\\temp\\detail-productdb-service.2017-11-29.log","UTF-8")

  //将文件的每行当成元素存入集合
  val list = source.getLines().toList
  val arr = ArrayBuffer[String]()

  //包含服务名的元素存入到动态集合
  list.map(x => if (x.contains("trans-pool-1-thread-") || x.contains("qbScheduler-")) arr += x else arr(arr.length - 1) += x)

  //对元素按照空格切割，得到的是二维数组
  val list2 = arr.map(_.split(" ", 6))
  val buffList = new ListBuffer[(String, Int)]

  //循环得出服务名称，key为服务名称，value为服务用时
  for (i <- 0 to list2.length - 1) {
    val ll = list2(i)(5).split(" ").filter(_.contains("com.")).filter(!_.contains("header")).map(_.replaceAll("\\(|\\)|\\:|\\[|\\]", ""))
    if (ll.length > 0) {
      buffList.append((ll(0), list2(i)(2).toInt))
    }
  }

  //按名称对服务分类，统计每个服务的使用次数
  var mapCount = Map[String,Int]()
  val groupList = buffList.groupBy(_._1).map(x => (x._1, x._2.size))
  for (x <- groupList){
    println("服务名："+x._1+"        调用次数："+x._2)
    mapCount += (x._1 -> x._2)
  }
  println("服务条数："+groupList.size)

  println("=========================================")

  //按名称分类，求每个服务平均时长
  val groupList2 = buffList.groupBy(_._1).map(x => (x._1,x._2.map(x => x._2).sum/x._2.size))
  for ( x <- groupList2){
    println( "服务名称："+x._1+"      服务平均时长："+ x._2)
  }

  println("=========================================")

  //求出调用次数top95和top99
  val mapSort = mapCount.toSeq.sortWith(_._2>_._2)
  val mapTop95 = mapSort.take(95)
  val mapTop99 = mapSort.take(99)


}
