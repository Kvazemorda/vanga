package com.tweakbit.controller.bean;

import com.tweakbit.controller.DataParser;
import com.tweakbit.model.Params;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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

    public  String safeParamsFromLandToMap(HttpServletRequest req, ServletContext context) throws IOException {
        if(req != null){
            DataParser dataParser = new DataParser(new StringBuilder(), initializeTheTreeOfParams(context), req);
            return dataParser.getToFile().toString();
        }else {
            return "null";
        }

    }
}
