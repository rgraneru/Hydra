package com.findwise.hydra.stage.webstages;

import net.htmlparser.jericho.*;
import com.findwise.hydra.local.LocalDocument;
import com.findwise.hydra.stage.AbstractProcessStage;
import com.findwise.hydra.stage.Parameter;
import com.findwise.hydra.stage.ProcessException;
import com.findwise.hydra.stage.RequiredArgumentMissingException;
import com.findwise.hydra.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roar Granevang
 */
@Stage(description = "This stage render a html page as text, removing all html tags, css styling and javascript")
public class RenderHTMLStage extends AbstractProcessStage {

	@Parameter(name = "fields", description = "The fields containing the html text to be rendered(removing html and such)")
	private List<String> fields;

	@Override
	public void init() throws RequiredArgumentMissingException {
		if (fields == null || fields.isEmpty()) {
			throw new RequiredArgumentMissingException("fields is missing. Need to know what fields to render");
		}
	}

	@Override
	public void process(LocalDocument doc) throws ProcessException {

		for (String field : fields) {
			Object objectToRender = doc.getContentField(field);
			// if list then render all listItems
			if (objectToRender instanceof List){
				List<String> stringList = (List) objectToRender;
				List<String> updatedList = new ArrayList();
				int size = stringList.size();
				for (int i = 0; i < size; i++) {
					updatedList.add(i, render(stringList.get(i)));
				}
				doc.putContentField(field, updatedList);
			}
			else if (objectToRender instanceof String){
				//assume string
				String stringToRender = (String) doc.getContentField(field);
				String rendered = render(stringToRender);
				doc.putContentField(field, rendered);			
			}
		}
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	private String render(String stringToRender) {
			if (stringToRender == null || stringToRender.isEmpty()) {
				return null;
			} else {
				Source source = new Source(stringToRender);
				String renderedString = source.getRenderer().toString();
				return renderedString;
			}
	}
}
