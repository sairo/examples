package org.jboss.seam.examples.booking.session;

import javax.annotation.Named;
import javax.annotation.PreDestroy;
import javax.context.RequestScoped;
import javax.ejb.Stateful;
import javax.inject.Current;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.examples.booking.Registered;
import org.jboss.seam.examples.booking.model.User;
import org.jboss.seam.international.StatusMessages;
import ui.RegistrationFormControls;

/**
 * @author Dan Allen
 */
public
@Named("passwordManager")
@Stateful
@RequestScoped
class PasswordManagerBean implements PasswordManager
{
   @PersistenceContext EntityManager em;

   @Current StatusMessages statusMessages;

   @Current RegistrationFormControls formControls;

   @Registered User user;

   private String confirmPassword;

   private boolean changed;

   public void changePassword()
   {
      if (user.getPassword().equals(confirmPassword))
      {
         // FIXME: dirty hack, can't merge a managed bean
         em.merge(new User(user.getName(), user.getUsername(), user.getPassword()));
         user.setPassword(null);
         statusMessages.add("Password successfully updated.");
         changed = true;
      }
      else
      {
         // FIME reverting isn't going to work here
         //revertUser();
         confirmPassword = null;
         statusMessages.addToControl(formControls.getConfirmPasswordControlId(), "Passwords do not match. Please re-type the new password.");
      }
   }

   public boolean isChanged()
   {
      return changed;
   }

   public void setConfirmPassword(String password)
   {
      this.confirmPassword = password;
   }

   public String getConfirmPassword()
   {
      return this.confirmPassword;
   }

   @PreDestroy
   public void destroy()
   {
   }
}