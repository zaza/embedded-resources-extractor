package com.github.zaza;

import static junitx.framework.FileAssert.assertEquals;
import java.io.File;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ExtractorTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void test1() throws Exception {
		assertExtracted("1/object_49_1.rtf", "1/file_0.docx");
	}

	@Test
	public void test2() throws Exception {
		assertExtracted("2/object_49_2.rtf", "2/file_0.pdf");
	}

	@Test
	public void test3() throws Exception {
		assertExtracted("3/object_49_3.rtf", "3/file_0.pdf", "3/file_1.pdf");
	}

	private void assertExtracted(String pathIn, String... pathsOut) throws Exception {
		InputStream in = getResourceAsStream(pathIn);
		File out = tempFolder.newFolder();

		new Extractor().extract(in, out);

		for (String pathOut : pathsOut) {
			assertEquals(readResource(pathOut), new File(out, pathOut.substring(pathOut.lastIndexOf('/'))));
		}
	}

	private InputStream getResourceAsStream(String path) {
		return getClass().getClassLoader().getResourceAsStream(path);
	}

	private File readResource(String path) {
		return new File(getClass().getClassLoader().getResource(path).getFile());
	}
}
