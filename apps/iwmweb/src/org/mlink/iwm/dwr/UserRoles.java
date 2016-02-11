package org.mlink.iwm.dwr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.UserRole;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.Role;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.lookup.RoleRef;
import org.mlink.iwm.session.PolicySF;

/**
 * User: andrei Date: Dec 4, 2006
 */
public class UserRoles implements ReturnCodes {

	public ResponsePage getData(HashMap criteria) throws Exception {
		SearchCriteria cr = new SearchCriteria(criteria);
		DAOResponse response = DaoFactory.process(DaoFactory.NAME.UserRolesDAO,
				cr);
		List lst = response.convertRowsToClasses(org.mlink.iwm.bean.UserRole.class);
		SessionUtil.removeAttribute("userRolesRemoved");
		SessionUtil.removeAttribute("userRolesAdded");
		return new ResponsePage(lst.size(), lst);
	}

	public ResponsePage getNewUserData() throws Exception {
		/** clear any previous selections */
		SessionUtil.removeAttribute("userRolesRemoved");
		SessionUtil.removeAttribute("userRolesAdded");

		List<UserRole> rtn = new ArrayList<UserRole>();
		Collection<OptionItem> roles = RoleRef.getRoles();
		for (OptionItem optionItem : roles) {
			UserRole role = new UserRole();
			Long roleId = optionItem.getValue();
			// add default roles
			if (roleId == RoleRef.getIdByCode(RoleRef.ConMR)
					|| optionItem.getValue() == RoleRef
							.getIdByCode(RoleRef.ConMW)) {
				role.setIsAssigned("1");
				addItem(roleId);
			} else {
				role.setIsAssigned("0");
			}
			role.setRoleId(roleId.toString());
			role.setDesc(optionItem.getLabel());
			rtn.add(role);
		}

		return new ResponsePage(rtn.size(), rtn);
	}

	public String addItem(Long roleId) throws Exception {
		String rtn = null;
		List<Long> roles = (List<Long>) SessionUtil
				.getAttribute("userRolesAdded");
		if (roles == null) {
			roles = new ArrayList<Long>();
			SessionUtil.setAttribute("userRolesAdded", roles);
		}
		if (!roles.contains(roleId))
			roles.add(roleId);
		return rtn;
	}

	public String deleteItem(Long roleId) throws Exception {
		String rtn = null;
		List<Long> roles = (List<Long>) SessionUtil
				.getAttribute("userRolesRemoved");
		if (roles == null) {
			roles = new ArrayList<Long>();
			SessionUtil.setAttribute("userRolesRemoved", roles);
		}
		if (!roles.contains(roleId))
			roles.add(roleId);
		return rtn;
	}

	/**
	 * Persist all updates queued in session
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String saveItem(Long userId) throws Exception {
		String rtn = ITEM_SAVED_OK_MSG;
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		List<Long> roleIds = (List<Long>) SessionUtil
				.getAttribute("userRolesRemoved");
		long roleIdVal;
		User user = psf.getUserNRoles(userId);
		Set<Role> roles = user.getRoles();
		Role role;
		if (roles != null) {
			Iterator<Role> iter = roles.iterator();
			if (roleIds != null) {
				for(Long roleId : roleIds) {
					roleIdVal = roleId.longValue();
					iter = roles.iterator();
					while (iter.hasNext()) {
						role = iter.next();
						if (role.getId() == roleIdVal) {
							iter.remove();
							break;
						}
					}
				}
			}
		}else{
			roles = new HashSet<Role>();
		}
		
		roleIds = (List<Long>) SessionUtil.getAttribute("userRolesAdded");
		if (roleIds != null) {
			for(Long roleId : roleIds) {
				role = psf.get(Role.class, roleId);
				roles.add(role);
			}
		}
		user.setRoles(roles);
		psf.update(user);
		return rtn;
	}

}
