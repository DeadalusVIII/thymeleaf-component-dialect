package de.morphbit.thymeleaf.processor;

import java.util.Map;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementModelStructureHandler;

import de.morphbit.thymeleaf.helper.FragmentHelper;

public class ComponentStandardElementProcessor
        extends ComponentNamedElementProcessor {
	
	private static final String TAG_NAME = "component";
	
	public ComponentStandardElementProcessor(String dialectPrefix) {
		super(dialectPrefix, TAG_NAME, null);
	}


	@Override
	protected void doProcess(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
		IProcessableElementTag tag = processElementTag(context, model);
		Map<String, String> attrMap = processAttribute(context, tag);

		IModel base = model.cloneModel();
		base.remove(0);
		
		if(base.size() > 1) {
			base.remove(base.size() - 1);
		}

		IModel frag = FragmentHelper.getFragmentModel(context,
		    attrMap.get("name"), structureHandler, THYMELEAF_FRAGMENT_PREFIX,
		    THYMELEAF_FRAGMENT_ATTRIBUTE);

		model.reset();
		model.addModel(mergeModel(frag, base, REPLACE_CONTENT_TAG));
	}

}
