/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.texteditor;

/**
 * Extension interface for <code>ITextEditor</code>. Adds the following functions:
 * <ul>
 * <li> modifiable state of the editor's input
 * <li> validate state of editor input
 * </ul>
 * 
 * @since 2.1
 */
public interface ITextEditorExtension2 {

	/**
	 * Returns whether the editor's input can be persistently be modified.
	 * This is orthogonal to <code>ITextEditorExtension.isEditorInputReadOnly</code> as read-only elements may be modifiable and
	 * writable elements may not be modifiable. If the given element is not connected to this document
	 * provider, the result is undefined. Document providers are allowed to use a cache to answer this
	 * question, i.e. there can be a difference between the "real" state of the element and the return
	 * value.
	 *
	 * @return <code>true</code> if the editor input is modifiable
	 *	 */
	boolean isEditorInputModifiable();

	/**
	 * Validates the state of the given editor input. The predominate intent
	 * of this method is to take any action propably necessary to ensure that
	 * the input can persistently be changed.
	 * 
	 * @return <code>true</code> if the input was validated, <code>false</code> otherwise
	 */
	boolean validateEditorInputState();

}
