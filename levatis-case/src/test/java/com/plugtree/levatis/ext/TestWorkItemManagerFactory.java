package com.plugtree.levatis.ext;

import org.drools.common.InternalKnowledgeRuntime;
import org.drools.process.instance.WorkItemManager;
import org.drools.process.instance.WorkItemManagerFactory;

public class TestWorkItemManagerFactory implements WorkItemManagerFactory {

	public WorkItemManager createWorkItemManager(InternalKnowledgeRuntime kruntime) {
		return new TestWorkItemManager(kruntime);
	}

}
