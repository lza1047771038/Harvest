package com.harvest.harvestwork.utils;

import com.harvest.core_base.impl.ContextResolver;
import com.harvest.core_base.interfaces.IContext;
import com.harvest.core_base.service.ServiceFacade;
import com.harvest.core_image.impl.ImageImpl;
import com.harvest.core_image_interface.interfaces.IImage;

public class AppStartUtils {

    public static void initWithOutPermission() {
        ServiceFacade.getInstance().put(IImage.class, new ImageImpl());
        ServiceFacade.getInstance().put(IContext.class, new ContextResolver());
    }
}
