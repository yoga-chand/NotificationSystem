package com.notification.watcher;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;


public class DirectoryWatcher {

	private static List<String> fileNames = new ArrayList<String>();	
	
	public List<String> watchDirectory(List<String> fileNames){
		
		try {
			
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get("D:/Venturesity/shared");
			dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);

			System.out.println("Watch Service registered for dir: " + dir.getFileName());

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return fileNames;
				}
				List <File> files = new LinkedList<File>();
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();
					files.add(fileName.toFile());
					System.out.println(kind.name() + ": " + fileName.getFileName().toString());
					

					if (kind == ENTRY_MODIFY || kind == ENTRY_CREATE) {
						System.out.println("My source file has changed!!!");
						fileNames.add(fileName.toString());
					}
				}
				
				
				Collections.sort(files, new Comparator<File>(){

					@Override
					public int compare(File f1, File f2) {
						// TODO Auto-generated method stub
						return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
					}
					
				});
				//Calling Fileparser to update the values in the table
				FileParser.createBooks(files);
				
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}

		return fileNames;
	}


	public static void main(String a[]){
		System.out.println(new Date().getTime());
		fileNames = new DirectoryWatcher().watchDirectory(fileNames);
		System.out.println("output "+fileNames);
	}
}
