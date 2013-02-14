package com.plugtree.levatis.ext;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

public abstract class AbstractWorkItemHandler implements WorkItemHandler {

	public void abortWorkItem(WorkItem item, WorkItemManager manager) {
		System.out.println(">>>>>>>>>>>  AbstractWorkItemHandler: task aborted");
	}

	public void executeWorkItem(WorkItem item, WorkItemManager manager) {
		System.out.println(">>>>>>>>>>>  AbstractWorkItemHandler: task about to start...");
		execute(item);
		System.out.println(">>>>>>>>>>>  AbstractWorkItemHandler: task started!!!");
	}

	protected abstract void execute(WorkItem item);
}
