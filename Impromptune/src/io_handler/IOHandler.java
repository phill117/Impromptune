package io_handler;

//import com.xenoage.zong.desktop.io.musicxml.in;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.desktop.io.ScoreDocIO;
import com.xenoage.zong.desktop.io.midi.out.MidiScoreDocFileOutput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreDocFileOutput;
import com.xenoage.zong.desktop.io.pdf.out.PdfScoreDocFileOutput;
import com.xenoage.zong.desktop.io.png.out.PngScoreDocFileOutput;
import com.xenoage.zong.documents.ScoreDoc;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by ben on 2/22/15.
 */
class IOHandler {
    private InputStreamReader isr = null;

//    public static void load() {
////        if (isr == null)
////            isr = new InputStreamReader();
//
//        FileChooser chooser = new FileChooser();
//        ExtensionFilter filter = new ExtensionFilter(
//                "MusicXML files", "mxl", "xml");
//        chooser.setSelectedExtensionFilter(filter);
//        chooser.setTitle("Select a file to load...");
//
//        int returnVal = chooser.showOpenDialog(parent);
//
//        if(returnVal == FileChooser.APPROVE) {
//            System.out.println("You chose to open this file: " +
//                    chooser.getSelectedFile().getName());
//
////            ScoreDoc read(InputStream stream, String filePath);
//        }
//
//
//
//    }

    /**
     * Saves the current score using the given format.
     * pdf, png, mid and ogg is supported by Zong.
     */

    public void saveAsType(String format) {
        FileOutput<ScoreDoc> out = null;
        switch (format) {
            case "pdf":
                out = new PdfScoreDocFileOutput();
                break;
            case "png":
                PngScoreDocFileOutput pngOut = new PngScoreDocFileOutput();
                pngOut.setJustOnePage(true);
                out = pngOut;
                break;
            case "mid":
                out = new MidiScoreDocFileOutput();
                break;
            case "ogg":
                out = new OggScoreDocFileOutput();
                break;
            default:
                return;
        }
    }

    public void saveAs() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "MusicXML files", "mxl", "xml");
        chooser.setSelectedExtensionFilter(filter);
        chooser.setTitle("Select a file to load...");

//        if (chooser.showOpenDialog(parent) == FileChooser.APPROVE) {
//            System.out.println("You chose to open this file: " +
//                    chooser.getSelectedFile().getName());
//            String filePath = "demo." + format;
//            try {
//                ScoreDocIO.write(currentScoreDoc, new File(filePath), out);
////            mainWindow.showMessageDialog(filePath + " saved.");
//            } catch (Exception ex) {
//                Err.handle(Report.error(ex));
//            }
//        }
    }
}
