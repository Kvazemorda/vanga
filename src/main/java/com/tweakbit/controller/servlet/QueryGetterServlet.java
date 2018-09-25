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

@WebServlet("/get_predict_for_du")
public class QueryGetterServlet extends HttpServlet {

    @EJB PrepareParamsForPredict initParamsForPredictBean;
    @EJB PredictModelBean learnedModel;



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try{
            ServletContext context = getServletContext();
            double anwser = learnedModel.predict(initParamsForPredictBean.safeParamsFromLandToMap(req, context), context);
            resp.setStatus(200);
            out.append(String.valueOf(anwser));
        }catch (NullPointerException e){
            e.printStackTrace();
            resp.setStatus(400);
            out.append("Error");
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET");
    }


}
