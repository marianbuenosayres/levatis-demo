package com.plugtree.levatis.ext;

import org.drools.runtime.process.WorkItem;

public class TestWorkItemHandler extends AbstractWorkItemHandler {

	private Long id;
	
	public void execute(WorkItem item) {
		this.id = item.getId();
		System.out.println(">>>>>>>>>>>  TestWorkItemHandler: specific task in process!!");
	}

	public Long getId() {
		return id;
	}
}
