package com.plugtree.levatis.repo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * This implementation should be changed to match yours.
 * 
 */
public class Repository {

	private static final Repository INSTANCE = new Repository();
	
	public static Repository getInstance() {
		return INSTANCE;
	}

	private Repository() {
	}
	
	/**
	 * @return all objects in the repository
	 * this implementation just returns 1 BPMN2 file.
	 */
	public List<Item> loadAll() {
		List<Item> ret = new ArrayList<Item>();
		ret.add(loadByName("process-demo"));
		return ret;
	}
	
	public Item loadByName(String name) {
		InputStream input1 = getClass().getResourceAsStream("/process-demo.bpmn2");
		Item item = new Item();
		try {
			item.setContent(IOUtils.toByteArray(input1));
		} catch (IOException e) {
			e.printStackTrace();
			item.setContent(null);
		}
		item.setFormat("bpmn2");
		item.setKey("process-demo");
		item.setName("process-demo");
		return item;
	}
	
	public Item loadByKey(String key) {
		return loadByName(key);
	}

	public boolean contains(String assetName) {
		return "process-demo".equals(assetName);
	}

	public void save(String name, byte[] content) {
		StringBuilder builder = new StringBuilder("BPMN2 saved!\n");
		builder.append(" Name: ").append(name).append('\n');
		builder.append("Content: \n");
		builder.append(new String(content));
		System.out.println(builder.toString());
	}

	public void delete(String assetName) {
		System.out.println("Trying to delete asset " + assetName);
	}
}
