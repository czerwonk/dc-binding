/* 
 * de.dan_nrw.binding
 * 
 * Copyright (C) 2010, Daniel Czerwonk <d.czerwonk@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dan_nrw.binding.tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.mockito.Mockito.*;

import com.jgoodies.binding.beans.Model;


/**
 * @author Daniel Czerwonk <d.czerwonk@googlemail.com>
 */
public class SetterAspectTests {

	@Test
	public void listeners_should_be_notified_when_modifing_integer_having_getter() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
		TestModel model = new TestModel();
		model.addPropertyChangeListener(listener);
		
		int value = 10;
		model.setIntValue(value);
		
        verify(listener, times(1)).propertyChange(argThat(new PropertyChangeEventVerifier("intValue", value)));
	}
	
	@Test
	public void listeners_should_be_notified_when_modifing_string_property_having_getter() {
	    PropertyChangeListener listener = mock(PropertyChangeListener.class);
        TestModel model = new TestModel();
        model.addPropertyChangeListener(listener);
		
        String value = "Test";
		model.setStringValue(value);
		
        verify(listener, times(1)).propertyChange(argThat(new PropertyChangeEventVerifier("stringValue", value)));
	}
	
	@Test
	public void listeners_should_not_be_notified_because_of_missing_getter() {
	    PropertyChangeListener listener = mock(PropertyChangeListener.class);
        TestModel model = new TestModel();
        model.addPropertyChangeListener(listener);
		
		model.setDummy("Test");
		
        verify(listener, never()).propertyChange(any(PropertyChangeEvent.class));
	}
	
	
	private static class PropertyChangeEventVerifier extends ArgumentMatcher<PropertyChangeEvent> {
	    
	    private final String expectedName;
	    private final Object expectedValue;
	    
	    
        /**
         * Creates a new instance of PropertyChangeEventValidator
         * @param expectedName
         * @param expectedValue
         */
        private PropertyChangeEventVerifier(String expectedName, Object expectedValue) {
            super();
            this.expectedName = expectedName;
            this.expectedValue = expectedValue;
        }

                
        /* (non-Javadoc)
         * @see org.mockito.ArgumentMatcher#matches(java.lang.Object)
         */
        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof PropertyChangeEvent)) {
                return false;
            }
            
            PropertyChangeEvent event = (PropertyChangeEvent)argument;
            return (event.getPropertyName().equals(this.expectedName)
                        && event.getNewValue().equals(this.expectedValue));            
        }
	}
	

	public static class TestModel extends Model {
		
        private static final long serialVersionUID = 4704806840311176317L;
 		private int intValue;
		private String stringValue;
		

        public int getIntValue() {
        	return this.intValue;
        }
		
        public void setIntValue(int intValue) {
        	this.intValue = intValue;
        }

        public String getStringValue() {
        	return this.stringValue;
        }
		
        public void setStringValue(String stringValue) {
        	this.stringValue = stringValue;
        }
        
        public void setDummy(Object obj) {
        	
        }
	}
}
