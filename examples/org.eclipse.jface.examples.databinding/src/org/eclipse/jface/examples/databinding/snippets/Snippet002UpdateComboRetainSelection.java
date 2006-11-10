/*******************************************************************************
 * Copyright (c) 2006 The Pampered Chef, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Pampered Chef, Inc. - initial API and implementation
 *     Brad Reynolds - bug 116920
 ******************************************************************************/

package org.eclipse.jface.examples.databinding.snippets;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.databinding.DataBindingContext;
import org.eclipse.jface.databinding.beans.BeansObservables;
import org.eclipse.jface.databinding.observable.IObservable;
import org.eclipse.jface.databinding.observable.Realm;
import org.eclipse.jface.databinding.observable.list.IObservableList;
import org.eclipse.jface.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.databinding.observable.masterdetail.MasterDetailObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Shows how to bind a Combo so that when update its items, the selection is
 * retained if at all possible.
 * 
 * @since 3.2
 */
public class Snippet002UpdateComboRetainSelection {
    public static void main(String[] args) {
        ViewModel viewModel = new ViewModel();
        Shell shell = new View(viewModel).createShell();

        // The SWT event loop
        Display display = Display.getCurrent();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // Print the results
        System.out.println(viewModel.getText());
    }

    // Minimal JavaBeans support
    public static abstract class AbstractModelObject {
        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    // The View's model--the root of our Model graph for this particular GUI.
    public static class ViewModel extends AbstractModelObject {
        private String text = "beef";

        private List choices = new ArrayList();
        {
            choices.add("pork");
            choices.add("beef");
            choices.add("poultry");
            choices.add("vegatables");
        }

        public List getChoices() {
            return choices;
        }

        public void setChoices(List choices) {
            List old = this.choices;
            this.choices = choices;
            firePropertyChange("choices", old, choices);
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            String oldValue = this.text;
            this.text = text;
            firePropertyChange("test", oldValue, text);
        }
    }

    // The GUI view
    static class View {
        private ViewModel viewModel;
        /**
         * used to make a new choices array unique
         */
        static int count;

        public View(ViewModel viewModel) {
            this.viewModel = viewModel;
        }

        public Shell createShell() {
            // Build a UI
            Shell shell = new Shell(Display.getCurrent());
            Realm.setDefault(SWTObservables.getRealm(shell.getDisplay()));
            shell.setLayout(new RowLayout(SWT.VERTICAL));

            Combo combo = new Combo(shell, SWT.BORDER | SWT.READ_ONLY);
            Button reset = new Button(shell, SWT.NULL);
            reset.setText("reset collection");
            reset.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    List newList = new ArrayList();
                    newList.add("Chocolate");
                    newList.add("Vanilla");
                    newList.add("Mango Parfait");
                    newList.add("beef");
                    newList.add("Cheesecake");
                    newList.add(Integer.toString(++count));
                    viewModel.setChoices(newList);
                }
            });

            // Print value out first
            System.out.println(viewModel.getText());

            DataBindingContext dbc = new DataBindingContext();
            
            IObservableList list = MasterDetailObservables.detailList(BeansObservables.observeValue(viewModel, "choices"),
                    getListDetailFactory(),
                    String.class);
            dbc.bindList(SWTObservables.getItems(combo), list, null); 
            dbc.bindValue(SWTObservables.getText(combo), BeansObservables.observeValue(viewModel, "text"), null);
            
            // Open and return the Shell
            shell.pack();
            shell.open();
            return shell;
        }
    }

    private static IObservableFactory getListDetailFactory() {
        return new IObservableFactory() {
            public IObservable createObservable(Object target) {
                WritableList list = new WritableList(String.class);
                list.addAll((Collection) target);
                return list;
            }
        };
    }
}
