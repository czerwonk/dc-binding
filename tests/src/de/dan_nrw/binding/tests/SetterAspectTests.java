package de.dan_nrw.binding.tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Test;
import static org.junit.Assert.*;

import com.jgoodies.binding.beans.Model;


/**
 * @author Daniel Czerwonk <d.czerwonk@googlemail.com>
 */
public class SetterAspectTests {

	@Test
	public void listeners_should_be_notified_when_modifing_integer_having_getter() {
		final TestResult result = new TestResult();
        TestModel model = new TestModel();		
		model.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
                public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("intValue")) {
						result.setResult(true);	
					}
                }
		});
		
		model.setIntValue(0);
		
		assertTrue(result.getResult());
	}
	
	@Test
	public void listeners_should_be_notified_when_modifing_string_property_having_getter() {
		final TestResult result = new TestResult();
        TestModel model = new TestModel();		
		model.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
                public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("stringValue")) {
						result.setResult(true);	
					}
                }
		});
		
		model.setStringValue("Test");
		
		assertTrue(result.getResult());
	}
	
	@Test
	public void listeners_should_not_be_notified_because_of_missing_getter() {
		final TestResult result = new TestResult();
        TestModel model = new TestModel();		
		model.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
                public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("stringValue")) {
						result.setResult(true);	
					}
                }
		});
		
		model.setDummy("Test");
		
		assertFalse(result.getResult());
	}
	

	private class TestResult {
		
		// Fields
		private boolean result;
		
        // Methods
        public boolean getResult() {
        	return this.result;
        }

        public void setResult(boolean result) {
        	this.result = result;
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
