package com.findwise.hydra.stage.tika;

import java.io.IOException;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.xml.sax.SAXException;

import com.findwise.hydra.DocumentFile;
import com.findwise.hydra.local.Local;
import com.findwise.hydra.local.LocalDocument;
import com.findwise.hydra.stage.*;
import com.findwise.hydra.stage.tika.utils.TikaUtils;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jwestberg
 * @author Roar Granevang
 */
@Stage(description = "Stage that fetches any files attached to the document being processed and parses them with Tika. Any fields found by Tika will be stored in <filename>_*")
public class TikaStage extends AbstractProcessStage {

    @Parameter(name = "addMetaData", description = "Add the metadata to the document or not. Defaults to true")
    private boolean addMetaData = true;
    @Parameter(description = "Set to true, will also do language detection and add the field 'prefix_language' according to the prefix rules. Defaults to true")
    private boolean addLanguage = true;
    @Parameter(description = "Only run for the given amount of seconds before aborting")
    private long timeout = 0;
    
    static private Parser parser = new AutoDetectParser();
    private static Logger logger = LoggerFactory.getLogger(TikaStage.class);

    @Override
    public void process(LocalDocument doc) throws ProcessException {
        try {
            List<String> files = getRemotePipeline().getFileNames(doc.getID());
            for (String fileName : files) {
                DocumentFile<Local> df = getRemotePipeline().getFile(fileName, doc.getID());
                if (timeout > 0){
                    TikaWithTimeout stuffToCall = new TikaWithTimeout(doc, fileName.replace('.', '_') + "_", df.getStream(), parser, addMetaData, addLanguage);
                    ExecutorService executor = Executors.newCachedThreadPool();
                    Future future = executor.submit(stuffToCall);
                    try {
                        future.get(timeout, TimeUnit.SECONDS);
                    } catch (InterruptedException ex) {                        
                        logger.warn("The stage was interrupted. Skipping this document.");
                    } catch (ExecutionException ex) {
                        logger.warn("The stage failed to execute. Skipping this document.");
                    } catch (TimeoutException ex) {
                        logger.warn("The stage timed out. Skipping this document.");
                    } finally{
                        executor.shutdown();
                    }                    
                }
                else{
                    TikaUtils.enrichDocumentWithFileContents(doc, fileName.replace('.', '_') + "_", df.getStream(), parser, addMetaData, addLanguage);                    
                }
            }
        } catch (IOException e) {
            throw new ProcessException("Failed opening or reading from stream", e);
        } catch (SAXException e) {
            throw new ProcessException("Failed parsing document", e);
        } catch (TikaException e) {
            throw new ProcessException("Got exception from Tika", e);
        }
    }
}
