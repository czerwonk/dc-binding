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
package de.dan_nrw.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.jgoodies.binding.beans.Model;


/**
 * @author Daniel Czerwonk <d.czerwonk@googlemail.com>
 */
@Aspect
public class SetterAspect {
	
	private static Pattern SETTER_PATTERN = Pattern.compile("^set(\\w)(.*)$");
	
	
	@Pointcut("execution(void com.jgoodies.binding.beans.Model+.set*(*))")
	void setterPointcut() {
		
	}
	
	@AfterReturning("setterPointcut()")
	public void afterReturingSetter(final JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();

		Method method = null;
		
		try {
			method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName().replace("set", "get"));	
		}
		catch (NoSuchMethodException ex) {
			// if no getter exists, there must be no update
			return;
		}
		
		if (method.getParameterTypes().length > 0) {
			// if getter expects arguments, method should abort
			return;
		}
		
		Matcher matcher = SETTER_PATTERN.matcher(methodSignature.getName());
		matcher.find();
		String propertyName = (matcher.group(1).toLowerCase() + matcher.group(2));
		
        try {
        	// getting new value
            Model model = (Model)joinPoint.getTarget();
        	Object newValue = method.invoke(model);
	        
			for (PropertyChangeListener listener : model.getPropertyChangeListeners()) {
				PropertyChangeEvent evt = new PropertyChangeEvent(model, propertyName, "", newValue);
				listener.propertyChange(evt);
			}
        }
        catch (InvocationTargetException ex) {
            ex.printStackTrace(System.err);
	        return;
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        	return;
        }
        catch (IllegalAccessException ex) {
            ex.printStackTrace(System.err);
        	return;
        }
	}
}