import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "buttonBean")
@SessionScoped
public class ButtonBean implements Serializable {

    public void destroyWorld(ActionEvent actionEvent) {
        
        System.out.println("*** Entro al metodo que imprime el Mensaje ***");
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Error", "Please try again later.");

        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
}