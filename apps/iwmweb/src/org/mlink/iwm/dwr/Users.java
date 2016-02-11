package org.mlink.iwm.dwr;

import java.util.HashMap;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * User: andrei
 * This action is used for password update
 * Date: Jan 15, 2007
 */
public class Users implements ReturnCodes{

    /**
     * Retrieves current user information
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.User getItem() throws Exception{
        String userName = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
        org.mlink.iwm.bean.User form = new org.mlink.iwm.bean.User();
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        User vo = psf.getUserByName( userName);
        CopyUtils.copyProperties(form,vo);
        form.setOldPassword(vo.getPassword());
        form.setPassword("");
        form.setPersonId(Long.toString(vo.getPerson().getId()));
        form.setUserId(Long.toString(vo.getId()));
        return form;
    }

    public String saveItem(HashMap map) throws Exception{
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        String rtn = ITEM_SAVED_OK_MSG;
        org.mlink.iwm.bean.User bean = new org.mlink.iwm.bean.User();
        CopyUtils.copyProperties(bean,map);
        try{
            psf.updateUserPassword(Long.valueOf(bean.getUserId()),bean.getOldPassword(),bean.getPassword());
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public String resetPassword(Long personId) throws Exception{
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        String rtn = ITEM_SAVED_OK_MSG;
        psf.resetPassword(personId);
        return rtn;
    }
}
