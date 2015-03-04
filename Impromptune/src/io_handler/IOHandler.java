package io_handler;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.desktop.io.ScoreDocIO;
import com.xenoage.zong.desktop.io.midi.out.MidiScoreDocFileOutput;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.io.ogg.out.OggScoreDocFileOutput;
import com.xenoage.zong.desktop.io.pdf.out.PdfScoreDocFileOutput;
import com.xenoage.zong.desktop.io.png.out.PngScoreDocFileOutput;
import com.xenoage.zong.desktop.io.print.PrintProcess;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by ben on 2/22/15.
 */
class IOHandler {
    public void load() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "MusicXML files", "mxl", "xml");
        chooser.setSelectedExtensionFilter(filter);
        chooser.setTitle("Select a file to load...");

//        if(chooser.showOpenDialog(parent) == FileChooser.APPROVE) {
//            ScoreDocIO.read(new File(chooser.getSelectedFile().getName()), new MusicXmlScoreDocFileInput());
//        }
    }

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
//        File f = chooser.showOpenDialog(parent);

//        if (f != null) {
//            String filePath = f.toURI().toURL().toString();
//            try {
//                ScoreDocIO.write(currentScoreDoc, new File(filePath), out);
//                Impromptune.PrimaryStage.showMessageDialog(filePath + " saved.");
//            } catch (Exception ex) {
//                Err.handle(Report.error(ex));
//            }
//        }
    }

    public void save() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "MusicXML files", "mxl", "xml");
        chooser.setSelectedExtensionFilter(filter);
        chooser.setTitle("Select a file to load...");

//        if (chooser.showOpenDialog(parent) == FileChooser.APPROVE) {
//
//            String filePath = chooser.getSelectedFile().getName());
//            try {
//                ScoreDocIO.write(currentScoreDoc, new File(filePath), out);
////            mainWindow.showMessageDialog(filePath + " saved.");
//            } catch (Exception ex) {
//                Err.handle(Report.error(ex));
//            }
//        }
    }

    public void print(Layout layout) {
        PrintProcess proc = new PrintProcess();
        proc.requestPrint(layout);
    }
}
