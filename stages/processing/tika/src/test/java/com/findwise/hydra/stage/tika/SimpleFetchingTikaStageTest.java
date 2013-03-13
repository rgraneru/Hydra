package com.findwise.hydra.stage.tika;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.findwise.hydra.local.LocalDocument;
import com.findwise.hydra.stage.ProcessException;
import java.net.URLDecoder;
import java.util.List;

public class SimpleFetchingTikaStageTest {

	private SimpleFetchingTikaStage stage;
	private LocalDocument doc;

	private String pattern = "attachment_(.*)";

	@Before
	public void init() {
		stage = new SimpleFetchingTikaStage();
		stage.setUrlFieldPattern(pattern);

		doc = new LocalDocument();
	}

	@Test(expected = RuntimeException.class)
	public void testProcess() throws Exception {

		doc.putContentField("attachment_a", "http://www.google.com");

		Parser parser = Mockito.mock(AutoDetectParser.class);
		stage.setParser(parser);

		Mockito.doThrow(new RuntimeException())
				.when(parser)
				.parse(Mockito.any(InputStream.class),
						Mockito.any(BodyContentHandler.class),
						Mockito.any(Metadata.class),
						Mockito.any(ParseContext.class));
		stage.process(doc);

	}
	
	@Test
	public void testListAttachments() throws Exception {
		doc.putContentField("attachment_links", Arrays.asList(new String[] {"http://www.google.com", "http://www.google.com", "http://www.google.com"}));
		
		stage.process(doc);
		
		Assert.assertTrue(doc.hasContentField("links_content"));
		Assert.assertTrue(doc.hasContentField("links2_content"));
		Assert.assertTrue(doc.hasContentField("links3_content"));
	}
	
//	@Test
	public void testURIEscaping() throws Exception {
		doc.putContentField("attachment_a", "http://google.com/ arbitrary path with spaces/");
		
		try {
			stage.process(doc);
			Assert.fail("Did not throw exception, path was incorrect");
		} catch(ProcessException e) {
			Assert.assertEquals(FileNotFoundException.class, e.getCause().getClass());
		}
	}
	
	//this is not really a unit itest. Its just code fi you need to test basic authentication
//	@Test 
	public void testGettingDocumentWithBasicAuth() throws Exception {
//		doc.putContentField("attachment", "http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP Flytskjema kjøkken.pdf");
//		doc.putContentField("attachment", "http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+gjennomgang+og+rapportering+for+2011.docx");
//		doc.putContentField("attachment", "http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Leivdeler.pdf");
//		doc.putContentField("attachment_a", "http://browserspy.dk/password-ok.php");
//		List<String> list = Arrays.asList(new String[] {"http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+gjennomgang+og+rapportering+for+2011.docx", "http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Prosessbeskrivelse+lager.heimdal.doc"});
//		doc.putContentField("attachment", list);
		String attachments = "http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Organisasjonskart+HACCP+gruppa+Tine+Heimdal.ppt;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+gjennomgang+og+rapportering+for+2011.docx;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+gjennomgang+og+rapportering+for+2012.docx;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Flowpack+L1+.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Flowpack+L2.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Rivost+L4.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Dypdrager+L5.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Dypdrager+L6.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Skiveost+L7.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Skiveost+L8.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Skiveost+L9.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Skiveost+L10.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Rivost+L11.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Rivost+L12.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Rivost+L13.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Skiveost+L15.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Skiveost+L16.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Leivdeler.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+Flytskjema+lager.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+Flytskjema+kj%C3%B8kken.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Prosessbeskrivelse+lager.heimdal.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+Prosessbeskrivelse+kantine.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/ATTHU4V1.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Fareskjema+Lager%2C+skjema1.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Fareskjema+kantine-skjema+1.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Farevurdering+pakkeri-skjema2.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Farevurdering+lager-skjema2.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Farevurdering+kantine-skjema+2.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/ATTXBTN7.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Bestemmelse+av+oPRP+og+CCP+etter+ISO22000+7.4.4-lager+skjema+8.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Bestemmelse+av+oPRP+og+CCP+etter+ISO22000+7.4.4-kantine+skjema+8.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+Grunnforutsetninger++Skjema+5+TINE+Heimdal.xlsx;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+-Grunnforutsetninger.Kantine%2C+Skjema+5..doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-oPRP-plan.+skjema+4.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+M%C3%98TEREFERAT.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP-Kriseberedskap-ISO22000+pkt+5.7.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Tegning+1.etg.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Tegning+kjeller.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Tegning+2etg+garderober.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Tegning+pakkelinjer.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Tegning+bed%C3%B8mmelse.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+SKjema+for+registreringer+funn%2C++metalldetektor.doc;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+Kontroll+av+glass%2C+hardplast+i+pakkeriet.xls;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+analyse+oppl%C3%A6ring.pptx;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Deltakere+p%C3%A5+Haccp+oppl%C3%A6ring+010212.Pakkerim%C3%B8te.pdf;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/HACCP+Kontroll+av+Glass%2Chardplast+kassevasker.xlsx;http://dstest01.tine.no:81/Drift/TineKvalDok.nsf/0/A846E44615A41FDFC125710A00432CAC/$FILE/Deltakere+p%C3%A5+Haccp+oppl%C3%A6ring+Lagerm%C3%B8te+27.01.2012.pdf";
		String[] list = attachments.split(";");
		doc.putContentField("attachment", Arrays.asList(list));
		
		stage.setUrlFieldPattern("attachment");

		stage.setUsername("_NotesIndexService");
		stage.setPassword("passord");
//			stage.setUsername("test");
//			stage.setPassword("test");
		stage.process(doc);
		System.out.println("");

		
	}

}