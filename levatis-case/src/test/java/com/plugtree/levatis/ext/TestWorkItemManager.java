package com.plugtree.levatis.ext;

import java.util.Map;

import org.drools.common.InternalKnowledgeRuntime;
import org.drools.process.instance.WorkItemManager;
import org.drools.process.instance.impl.DefaultWorkItemManager;

public class TestWorkItemManager extends DefaultWorkItemManager implements
		WorkItemManager {

	public TestWorkItemManager(InternalKnowledgeRuntime kruntime) {
		super(kruntime);
	}
	
	@Override
	public void completeWorkItem(long id, Map<String, Object> results) {
		System.out.println(">>>>>>>>>>>  TestWorkItemManager: Printed BEFORE completing item");
		super.completeWorkItem(id, results);
		System.out.println(">>>>>>>>>>>  TestWorkItemManager: Printed AFTER completing item");
	}

}
