package com.le.service.admin;

import com.le.model.bo.menu.MenuDeleteBO;
import com.le.model.bo.menu.MenuSaveBO;
import com.le.model.bo.menu.MenuTreeBO;
import com.le.model.bo.menu.MenuUpdateBO;
import com.le.model.vo.menu.MenuDetailVO;
import com.le.model.vo.menu.MenuOptionTreeNodeVO;
import com.le.model.vo.menu.MenuTreeNodeVO;

import java.util.List;

/**
 * 菜单管理接口
 *
 * @author Bruce Lu
 */
public interface MenuService {


	/**
	 * 菜单树
	 *
	 * @param treeBO
	 * @return 菜单树
	 */
	List<MenuTreeNodeVO> listMenuTree(MenuTreeBO treeBO);

	/**
	 * 菜单树下拉选项列表
	 * 注:只查启用
	 *
	 * @param treeBO
	 * @return 菜单树
	 */
	List<MenuOptionTreeNodeVO> listMenuOptionTree(MenuTreeBO treeBO);


	/**
	 * 菜单详情
	 *
	 * @param menuId
	 * @return 菜单详情
	 */
	MenuDetailVO getMenuDetail(Long menuId);

	/**
	 * 新增菜单
	 *
	 * @param saveBO
	 * @author Bruce Lu
	 */
	void addMenu(MenuSaveBO saveBO);

	/**
	 * 修改菜单
	 *
	 * @param updateBO
	 * @author Bruce Lu
	 */
	void updateMenu(MenuUpdateBO updateBO);

	/**
	 * 删除菜单（逻辑删）
	 *
	 * @param deleteBO
	 * @author Bruce Lu
	 */
	void deleteMenu(MenuDeleteBO deleteBO);

}
