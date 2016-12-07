/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package class2;

import java.util.ArrayList;

/**
 *
 * @author vcl4
 */
public class deviceTypeData {
    
    private ArrayList<String> company;
    private ArrayList<String> classification;
    private ArrayList<String> model;
    
    
    public deviceTypeData ()
    {
        company = new ArrayList<String> ();
        classification = new ArrayList<String> ();
        model = new ArrayList<String> ();
    }

    public ArrayList<String> getCompany() {
        return company;
    }

    public void addCompany(String company) {
        this.company.add(company);
    }

    public ArrayList<String> getClassification() {
        return classification;
    }

    public void addClassification(String classification) {
        this.classification.add(classification);
    }

    public ArrayList<String> getModel() {
        return model;
    }

    public void addModel(String model) {
        this.model.add(model);
    }
    
    
    
}
