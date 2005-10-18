package org.eclipse.ui.views.markers.internal;

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.progress.DeferredTreeContentManager;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;

/**
 * The MarkerTreeContentProvider is the content provider for the marker trees.
 * 
 * @since 3.2
 * 
 */
public class MarkerTreeContentProvider implements ITreeContentProvider {

	private class MarkerContentManager extends DeferredTreeContentManager {

		/**
		 * Create a new instance of the receiver.
		 * 
		 * @param provider
		 * @param viewer
		 * @param site
		 */
		public MarkerContentManager(ITreeContentProvider provider,
				AbstractTreeViewer viewer, IWorkbenchPartSite site) {
			super(provider, viewer, site);
		}
	}

	MarkerContentManager manager;

	TreeViewer viewer;

	IDeferredWorkbenchAdapter input;

	IWorkbenchPartSite partSite;

	boolean hierarchalMode = true;

	/**
	 * Create a new content provider for the view.
	 * 
	 * @param site
	 */
	MarkerTreeContentProvider(IWorkbenchPartSite site) {
		partSite = site;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof MarkerNode)
			return ((MarkerNode) parentElement).getChildren();
		return Util.EMPTY_MARKER_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		if(element instanceof MarkerNode)
			return ((MarkerNode) element).getParent();
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if(element instanceof MarkerNode)
			return ((MarkerNode) element).getChildren().length > 0;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		return manager.getChildren(inputElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// Nothing to do here.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		manager = new MarkerContentManager(this, this.viewer, partSite);
		input = (IDeferredWorkbenchAdapter) newInput;

	}

	/**
	 * Refresh the children without changing the widget yet.
	 */
	public void refresh() {
		getChildren(input);
	}

}
