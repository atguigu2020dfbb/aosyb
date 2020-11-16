package cn.osworks.aos.system.modules.controller.retrieval;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.asset.WebCxt;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.po.Archive_tablefieldlistPO;
import cn.osworks.aos.system.modules.service.archive.DataService;
import cn.osworks.aos.system.modules.service.archive.DocService;
import cn.osworks.aos.system.modules.service.retrieval.RetrievalService;


/**
 * 
 * 全文检索
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="retrieval/retrieval")
public class RetrievalController {
	
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private RetrievalService retrievalService;
	
	@Autowired
	private DocService docService;
	/**
	 * 
	 * 页面初始化
	 * 
	 * @return
	 */
	@RequestMapping(value="initRetrieval")
	public String initCheck(){
		
		//return "aos/retrieval/retrieval.jsp";
		return "aos/retrieval/retrieval.jsp";
	}
	
	/**
	 * 
	 * 创建索引
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="listAccounts")
	public void listAccounts(HttpServletRequest request, HttpServletResponse response){
		// inDto包装了全部的请求参数哦
		Dto qDto = Dtos.newDto(request);
		qDto.put("tablename", "wsgdwj");
		List<Dto> pathDtos=dataService.getAllPath(qDto);
		PDDocument pddocument = null;
		/**放索引文件的位置*/
        try {
        	File indexDir=new File("D:\\lucene\\index");
			Directory  dir=FSDirectory.open(indexDir);
			Analyzer luceneAnalyer=new StandardAnalyzer(Version.LUCENE_36);
			IndexWriterConfig iwc=new IndexWriterConfig(Version.LUCENE_36,luceneAnalyer);
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter indexWriter=new IndexWriter(dir,iwc);
    		for(Map<String, Object> e : pathDtos) {  
    			File pdfFile = new File(e.get("path").toString());
    			pddocument=PDDocument.load(pdfFile);
    	        PDFTextStripper stripper = new PDFTextStripper();  
    	        String body = stripper.getText(pddocument);  
    	        //String body="第一条 为推动电子档案科学管理，规范电子档案管理系统建设，明确电子档案管理系统基本功能，确保电子档案真实、完整、可用与安全，按照国家有关法律法规和标准规范，制定本规定";
    			Document document=new Document();
    			Field fieldid=new Field("id_",e.get("id_").toString(),Field.Store.YES,Field.Index.ANALYZED,TermVector.WITH_POSITIONS_OFFSETS);
    			Field fieldtm=new Field("tm",e.get("tm").toString(),Field.Store.YES,Field.Index.ANALYZED,TermVector.WITH_POSITIONS_OFFSETS);
    			Field fieldbody=new Field("body",body,Field.Store.YES,Field.Index.ANALYZED,TermVector.WITH_POSITIONS_OFFSETS);
    			 document.add(fieldid);
    			 document.add(fieldtm);
    			 document.add(fieldbody);
    			 indexWriter.addDocument(document);
    	        // String aa = e.get("_s_path").toString();
    	    }  
    		indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {  
	        if (pddocument != null) {  
	                try {
						pddocument.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
	        }  
	    }  
	}
	/**
	 *
	 * 打开电子文件
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "openPdfFile")
	public String openPdfFile(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
		String temp = "";
		Dto inDto = Dtos.newDto(request);
		String tablename=inDto.getString("tablename");
		Properties prop = PropertiesLoaderUtils
				.loadAllProperties("config.properties");
		String linkAddress = prop.getProperty("linkAddress");
		String path1 = retrievalService.getDocumentPath(inDto);
		String path = linkAddress + path1;
		List<Archive_tablefieldlistPO> titleDtos = dataService.getInfoFieldListTitle(inDto.getString("tablename") + "_info");
		request.setAttribute("tablename", inDto.getString("tablename"));
		request.setAttribute("id", inDto.getString("id"));
		request.setAttribute("strswf", path);
		request.setAttribute("tid", inDto.getString("tid"));
		request.setAttribute("fieldDtos", titleDtos);
		return "common/documentView.jsp";
	}
	/**
	 * 
	 * 查旬索引
	 * 
	 * @param request
	 * @param response
	 * @throws ParseException
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 */
	@RequestMapping(value="getDataList")
	public void getDataList(HttpServletRequest request,
			HttpServletResponse response) throws ParseException, IOException, InvalidTokenOffsetsException {
		//String index = "D:\\lucene\\index";
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String index = prop.getProperty("filePath")+"/luceneIndex";
		Dto qDto = Dtos.newDto(request);
		File file = new File(index);
		IndexReader reader = null;
		String outString=null;
		try {
			reader = IndexReader.open(FSDirectory.open(file));
			IndexSearcher searcher = new IndexSearcher(reader);
			String queryStr = qDto.getString("content");// 搜索的关键词
			Query query = null;
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			QueryParser qp = new QueryParser(Version.LUCENE_36, "body",
					analyzer);
			query = qp.parse(queryStr);
			
			QueryScorer scorer = new QueryScorer(query);// 查询得分
	        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);// 得到得分的片段，就是得到一段包含所查询的关键字的摘要
	        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
	                "<b><font color='red'>", "</font></b>");
	        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);// 根据得分和格式化
	        highlighter.setTextFragmenter(fragmenter);// 设置成高亮
	        List<Map<String, Object>> fieldDtos = new ArrayList<Map<String, Object>>();
	        TopDocs hits = searcher.search(query, 100);
	        for (ScoreDoc scoreDoc : hits.scoreDocs) {
	            Document doc = searcher.doc(scoreDoc.doc);
	            String desc = doc.get("body");
	            if (desc != null) {
	                TokenStream tokenStream = analyzer.tokenStream("desc",
	                        new StringReader(desc));// TokenStream将查询出来的搞成片段，得到的是整个内容
	                Dto inDto = Dtos.newDto();
	                inDto.put("nrzy", highlighter.getBestFragment(tokenStream,
					        desc));
	                //inDto.put("tm", doc.get("tm"));
	                Dto dto = retrievalService.selectId(doc.get("id_"));
	                //String tm=(String)getString("tm");
	                inDto.put("id_", doc.get("id_"));
	                inDto.put("pathid", doc.get("pathid"));
					inDto.put("tm", doc.get("tm"));
					inDto.put("dh", doc.get("dh"));
					inDto.put("zrz", doc.get("zrz"));
					inDto.put("bgqx", doc.get("bgqx"));
					inDto.put("dalb", doc.get("dalb"));
					inDto.put("tablename", doc.get("tablename"));
	                inDto.putAll(dto);
	       		 fieldDtos.add(inDto);
	            }
	        }
	        int pCount=fieldDtos.size();
	        outString = AOSJson.toGridJson(fieldDtos, pCount);
	        reader.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * 获得电子文件信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getPath")
	public void getPath(HttpServletRequest request,HttpServletResponse response){
		Dto inDto = Dtos.newDto(request);
		List<Dto> pathDtos=dataService.getPath(inDto);
		
//		request.setAttribute("pathDtos", pathDtos);
		
		String outString =AOSJson.toGridJson(pathDtos);
//		Dto outDto = printService.getPath(inDto);
		WebCxt.write(response, outString);
	}
	
	/**
	 * 
	 * 打开电子文件
	 * 
	 * @author Sun
	 *
	 * 2018-8-27 
	 */
	@RequestMapping(value="openFile")
	public String openFile(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Dto inDto = Dtos.newDto(request);
		String temp="";
		int pageCount=1;
		Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
		String base= prop.getProperty("filePath")+request.getParameter("tablename");//获取username key对应的值 
	       inDto.put("base", base);
		String  path1=dataService.getDocumentPath(inDto);
		//D://dataaos/wsda/4bcd26dbc6fa409ab1d1e6f182d8241a/a0d6df862dbc4abab16d511d0978e02f.jpg
		if(inDto.getString("type").indexOf("jpg")>-1){
			pageCount = docService.jpeg2swf(path1);
		}if(inDto.getString("type").indexOf("pdf")>-1){
			//pageCount = docService.pdf2swf(path1);
		}if(inDto.getString("type").indexOf("png")>-1){
			pageCount = docService.png2swf(path1);
		}
		List<Archive_tablefieldlistPO> titleDtos = dataService.getInfoFieldListTitle(inDto.getString("tablename")+"_info");
		String count=String.valueOf(pageCount);
		request.setAttribute("tablename", inDto.getString("tablename"));
		request.setAttribute("id", inDto.getString("id"));
		request.setAttribute("count", count);
		request.setAttribute("tid", inDto.getString("tid"));
		request.setAttribute("fieldDtos", titleDtos);
		return "common/documentView.jsp";
		
		
	}
	
	
	/**
	 * 
	 * 获得电子文件信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="addIndex")
	public void addIndex(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//Dto inDto = Dtos.newDto(request);
		//List<Dto> pathDtos=dataService.getPath(inDto);
		
//		request.setAttribute("pathDtos", pathDtos);
		Dto out=retrievalService.addIndex();
		
		
		String outString =AOSJson.toJson(out);
//		Dto outDto = printService.getPath(inDto);
		WebCxt.write(response, outString);
	}
	/**
	 *
	 * 获得电子文件信息
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="addIndex2")
	public void addIndex2(HttpServletRequest request,HttpServletResponse response) throws IOException, URISyntaxException {
		//Dto inDto = Dtos.newDto(request);
		/*CloseableHttpClient client = HttpClientBuilder.create().build();

		URIBuilder uri = null;
		CloseableHttpResponse response = null;
		try {
			uri = new URIBuilder("http://5d5b453deb366.freetunnel.cc/YHOA/restservices/oarest/SyncentryDataFull/query");
			uri.setCharset(Charset.forName("UTF-8"));
			HttpGet hg = new HttpGet(uri.build());
			// 设置请求的报文头部的编码
			hg.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
			// 设置期望服务端返回的编码
			hg.setHeader(new BasicHeader("Accept", "text/plain;charset=UTF-8"));
			// 请求服务
			response = client.execute(hg);
			// 获取响应码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				// 获取返回实例entity
				//HttpEntity entity = response.getEntity();
				// 通过EntityUtils的一个工具方法获取返回内容
				String result= EntityUtils.toString(response.getEntity());

				// JSONObject json_test = JSONObject.fromObject(result);
				// json_test.getString("aa");
				System.out.println("3333");
				//JSONObject json = JSONObject.p
				//  System.out.println(result);
				// JSONObject obj = JSONObject.parseObject(result);

				// 输出
			} else {
				// 输出
				System.out.println("请求失败,错误码为: " + statusCode);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				// 释放资源
				if (client != null) {
					client.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	
}
