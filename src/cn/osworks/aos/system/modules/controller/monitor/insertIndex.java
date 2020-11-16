package cn.osworks.aos.system.modules.controller.monitor;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import cn.osworks.aos.core.id.AOSId;

public class insertIndex extends JdbcDaoSupport{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args){
		
		//tid  主表主建
		
		//pathid 电子表主键
		
		//filename 电子文件路径
		
//		//
//		String sql="SELECT a as path From allpath";
//		List<Map<String, Object>> listDto = jdbcTemplate.queryForList(sql);
//		String path = listDto.get(0).get("path").toString();
//		
//		String strpathid=AOSId.uuid();
//		java.util.Properties prop = PropertiesLoaderUtils.loadAllProperties("config.properties");
//		String lucenIndex =prop.getProperty("filePath")+"/luceneIndex";
//		File file = new File(lucenIndex);
//		if(file.exists()){
//			file.mkdirs();
//		}
//		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
//		Directory directory = FSDirectory.open(new File(file.toString()));
//
//		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,
//				analyzer);
//		IndexWriter indexWriter = new IndexWriter(directory, config);
//
//		Document doc = new Document();
//		// indexWriter.addDocument(doc1);
//		File pdfFile = new File(qDto.getString("fileName"));
//		PDFTextStripper stripper = new PDFTextStripper();
//		PDDocument pddocument = PDDocument.load(pdfFile);
//		String body = stripper.getText(pddocument);
//		// Dto tm = dataService.selectOne(id,tablename);
//		Field fieldid = new Field("id_", qDto.getString("tid"), Field.Store.YES,
//				Field.Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS);
//		Field pathid = new Field("pathid", strpathid, Field.Store.YES,
//				Field.Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS);
//		Field fieldbody = new Field("body", body, Field.Store.YES,
//				Field.Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS);
//		doc.add(fieldid);
//		//doc.add(fieldtm);
//		doc.add(fieldbody);
//		doc.add(pathid);
//		indexWriter.addDocument(doc);
//		indexWriter.commit();
//		indexWriter.close();
		
	}
	

}
