package com.harvest.harvestwork.utils;

import com.harvest.core_base.impl.ContextResolver;
import com.harvest.core_base.interfaces.IContext;
import com.harvest.core_base.service.ServiceFacade;
import com.harvest.core_image.impl.ImageImpl;
import com.harvest.core_image.impl.ImageLoaderImpl;
import com.harvest.core_image_interface.interfaces.IImage;
import com.harvest.core_image_interface.interfaces.IImageLoader;

public class AppStartUtils {

    public static void initWithOutPermission() {
        ServiceFacade.init();
        ServiceFacade.getInstance().put(IImage.class, new ImageImpl());
        ServiceFacade.getInstance().put(IImageLoader.class, new ImageLoaderImpl());
        ServiceFacade.getInstance().put(IContext.class, new ContextResolver());
    }
}
