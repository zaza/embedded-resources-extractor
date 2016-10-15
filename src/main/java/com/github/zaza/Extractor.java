package com.github.zaza;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.ContainerExtractor;
import org.apache.tika.extractor.EmbeddedResourceHandler;
import org.apache.tika.extractor.ParserContainerExtractor;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.mime.MediaType;
import org.xml.sax.SAXException;

public class Extractor {
	public static void main(String[] args) throws IOException, SAXException, TikaException {
		new Extractor().extract(new File(args[0]), new File(args[1]));
	}

	private void extract(File in, File out) throws IOException, SAXException, TikaException {
		try (InputStream inputStream = new FileInputStream(in)) {
			extract(inputStream, out);
		}
	}

	void extract(InputStream inputStream, File out) throws IOException, SAXException, TikaException {
		TikaInputStream tikaStream = TikaInputStream.get(inputStream);
		ContainerExtractor ex = new ParserContainerExtractor();
		EmbeddedResourceHandler handler = new SaveToFileHandler(out);
		ex.extract(tikaStream, ex, handler);
	}

	private static class SaveToFileHandler implements EmbeddedResourceHandler {
		private File out;

		public SaveToFileHandler(File out) {
			this.out = out;
		}

		@Override
		public void handle(String filename, MediaType mediaType, InputStream stream) {
			try {
				Path path = Paths.get(out.getAbsolutePath(), filename);
				Files.copy(stream, path);
				System.out.println("Wrote embedded resource to: " + path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
