package com.tweakbit.controller.servlet;

import com.tweakbit.controller.bean.PredictModelBean;
import com.tweakbit.controller.bean.PrepareParamsForPredict;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

@WebServlet("/get_predict_for_du")
public class QueryGetterServlet extends HttpServlet {

    @EJB PrepareParamsForPredict initParamsForPredictBean;
    @EJB PredictModelBean learnedModel;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            ServletContext context = getServletContext();
            resp.setStatus(200);
            resp.setContentType("text/xml");
            PrintWriter writer=resp.getWriter();
            String anwser = learnedModel.predict(initParamsForPredictBean.safeParamsFromLandToMap(req, context), context);
            System.out.println("answer = " + anwser);
            writer.println(anwser);
            writer.append(anwser);
        }catch (NullPointerException e){
            e.printStackTrace();
            resp.setStatus(400);
            resp.setContentType("text/xml");
            PrintWriter writer=resp.getWriter();
            writer.println("Error");
            writer.append("Error");
        }

    }


}
