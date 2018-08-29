package com.tweakbit.controller.bean;

import com.tweakbit.controller.DataParser;
import com.tweakbit.driverupdater.model.enties.VisitToTweakBit;
import com.tweakbit.model.Params;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.TreeMap;

@Local
@Stateless
public class PrepareParamsForPredict {

    private TreeMap<Params,TreeMap<String,Integer>> initializeTheTreeOfParams(ServletContext context) {
        TreeMap<Params,TreeMap<String,Integer>> trees = null;
        try {
            URL treeOfParams = context.getResource("/WEB-INF/classes/duparams/treesOfParams.txt");
            InputStream fis = treeOfParams.openStream();
            ObjectInputStream ois = new ObjectInputStream(fis);
            trees = (TreeMap<Params,TreeMap<String,Integer>>) ois.readObject();
            System.out.println("----------- " + trees.size());
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return trees;
    }

    public  String safeParamsFromLandToMap(HttpServletRequest req, ServletContext context){
        if(req != null){
            VisitToTweakBit app = new VisitToTweakBit();
            DataParser dataParser = new DataParser(new StringBuilder(), app, initializeTheTreeOfParams(context), req);
            return dataParser.getToFile().toString();
        }else {
            return "null";
        }

    }
}
