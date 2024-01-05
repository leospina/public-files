package com.credibanco.ssh;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public class TarExtractor  {

	 private InputStream tarStream;
	 private boolean gzip;
	 private Path destination;
	    
    protected TarExtractor(InputStream in, boolean gzip, Path destination) throws IOException {
    	tarStream =in;
    	this.gzip = gzip;
    	this.destination = destination ;
    	Files.createDirectories(destination);
    }

    public void untar() throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(getTarStream());
          TarArchiveInputStream tar = new TarArchiveInputStream(
          isGzip() ? new GzipCompressorInputStream(inputStream) : inputStream)) {
            ArchiveEntry entry;
            while ((entry = tar.getNextEntry()) != null) {
                Path extractTo = getDestination().resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(extractTo);
                } else {
                    Files.copy(tar, extractTo,StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    
    public Path getDestination() {
        return destination;
    }

    public InputStream getTarStream() {
        return tarStream;
    }

    public boolean isGzip() {
        return gzip;
    }
    
}
