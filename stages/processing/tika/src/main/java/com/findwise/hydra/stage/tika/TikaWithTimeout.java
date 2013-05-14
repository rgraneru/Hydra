package com.findwise.hydra.stage.tika;

import com.findwise.hydra.local.LocalDocument;
import com.findwise.hydra.stage.tika.utils.TikaUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.Parser;
import org.xml.sax.SAXException;

public class TikaWithTimeout implements Callable {
    private final LocalDocument doc;
    private final String fileName;
    private final InputStream stream;
    private final Parser parser;
    private final boolean addMetaData;
    private final boolean addLanguage;

    TikaWithTimeout(LocalDocument doc, String fileName, InputStream stream, Parser parser, boolean addMetaData, boolean addLanguage) throws IOException, SAXException, TikaException {
        this.doc = doc;
        this.fileName = fileName;
        this.stream = stream;
        this.parser = parser;
        this.addMetaData = addMetaData;
        this.addLanguage = addLanguage;
    }
    @Override
    public Boolean call() throws Exception {
        TikaUtils.enrichDocumentWithFileContents(doc, fileName, stream, parser, addMetaData, addLanguage);
        return Boolean.TRUE;
    }
}
