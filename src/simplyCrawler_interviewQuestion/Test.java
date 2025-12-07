package simplyCrawler_interviewQuestion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Classname Test
 * @Description
 * @Date 2025/12/5 20:46
 * @Created by 666
 */
public class Test {
    public static void main(String[] args) throws IOException {
        //方便后续重复利用
        String url = "https://www.mianshiya.com/";
        File file =null;
        Document document = Jsoup.connect("https://www.mianshiya.com/").get();
        //找标签的时候注意多看几个标签有可能都是一样的
        Elements elements = document.getElementsByClass("ant-card-body");
        for (Element element :elements){
            Element first = element.getElementsByClass("ant-card-meta-title").first();
//            System.out.println(first);
            if(first!=null){
                 String html = first.html();
//                System.out.println(html);
                //创建文件接收
                 file = new File("D:\\Crawler_JavaInterview",html);
                if(!file.exists()){
                    file.mkdirs();
                }
            }
            //进行跳转,看页面路径，是原先路径拼接上去的
            Element a = element.getElementsByTag("a").first();
            if(a!=null){
                //得到路径名
                String href = a.attr("href");
                Document document1= Jsoup.connect(url+href).get();
                //一次全部爬完，进行改页面，一共200条，把这200条放在一面（原网页是分开放在了20页）
                Document document2 = Jsoup.connect(url+href+"?current=1&pageSize=200").get();
                Elements row = document1.getElementsByClass("ant-table-row ant-table-row-level-0");
                for(Element element1 : row){
                    Elements toSearchVIP = element1.getElementsByClass("ant-table-cell");
//                    if (!toSearchVIP.get(3).equals("VIP")){
//
//                    }
//                    System.out.println(toSearchVIP.get(2));
                    Element element2 = toSearchVIP.get(2);
                    Element element3 = element2.getElementsByClass("ant-space-item").first();
                    String VIP = element3.getElementsByTag("span").first().html();
//                    System.out.println(VIP);
                    if(!VIP.equalsIgnoreCase("VIP")){
//                        System.out.println(toSearchVIP.first());
                        Elements a1 = toSearchVIP.first().getElementsByTag("a");
                        String fileName = toSearchVIP.first().getElementsByTag("a").html();
//                        System.out.println(fileName);
                        //文件名不能包含一些符合
                        fileName = fileName.replace('\\','_')
                                .replace('*','_')
                                .replace('?','_')
                                .replace('<','_')
                                .replace('>','_')
                                .replace(':','_')
                                .replace('\"','_')
                                .replace('|','_')
                                .replace('/','_');
                        //下面写法错误，String不可变，要重新赋值
//                        fileName.replace('\\','_');
//                        fileName.replace('*','_');
//                        fileName.replace('?','_');
//                        fileName.replace('<','_');
//                        fileName.replace('>','_');
//                        fileName.replace(':','_');
//                        fileName.replace('\"','_');
//                        fileName.replace('|','_');
//                        fileName.replace('/','_');
                        File file1 = new File(file,fileName+".html");
                        if(!file1.exists()){
                            file1.createNewFile();
                        }
//                        System.out.println(a1);
                        String href1 = a1.attr("href");
                        Document document3 = Jsoup.connect(url +  href1).get();
                        Elements elements1 = document3.getElementsByClass("ant-card-body");
//                        System.out.println(elements1.get(1));
                        Element markdown = elements1.get(1).getElementsByClass("markdown-body").first();
//                        System.out.println(markdown);
                        if(markdown!=null){
                            String html =markdown.html();
                            //在 HTML 中，< 会被转义成 &lt;（完整是 &lt;，不是 &lt），> 会被转义成 &gt;
                            //为了避免浏览器把符号当成标签解析
                            //扩展
                            //&amp;（对应 &）：如果图片 URL 里有 & 被转义成 &amp;，不替换的话路径会错误；
                            //&quot;（对应 "）：图片 src 的引号被转义，标签会失效。
                            html = html.replaceAll("&lt;", "<")
                                    .replaceAll("&gt;", ">")
                                    .replaceAll("&amp;", "&")
                                    .replaceAll("&quot;", "\"");
                            BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
                            bw.write(html);
                            bw.close();
                        }

                    }

                }
            }
        }
    }
}