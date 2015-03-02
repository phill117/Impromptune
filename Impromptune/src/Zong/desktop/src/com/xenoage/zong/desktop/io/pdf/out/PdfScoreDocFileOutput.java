package com.xenoage.zong.desktop.io.pdf.out;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.desktop.io.print.PdfPrinter;
import com.xenoage.zong.documents.ScoreDoc;

import java.io.IOException;

/**
 * This class writes a PDF file from a given {@link ScoreDoc}
 * 
 * @author Andreas Wenger
 */
public class PdfScoreDocFileOutput
	implements FileOutput<ScoreDoc> {

	@Override public void write(ScoreDoc document, com.xenoage.utils.io.OutputStream stream,
		String filePath)
		throws IOException {
		PdfPrinter.print(document.getLayout(), new JseOutputStream(stream));
	}

	@Override public boolean isFilePathRequired(ScoreDoc document) {
		return false;
	}

}
