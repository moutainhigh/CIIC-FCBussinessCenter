package com.ciicsh.gto.fcsupportcenter.tax.util.page;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * 包含“分页”信息的List
 * 
 * <p>要得到总页数请使用 toPaginator().getTotalPages();</p>
 *
 * Created by shil on 2017/9/20.
 *
 */
public class PageList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 1412759446332294208L;
    
    private com.ciicsh.gto.fcsupportcenter.tax.util.page.Paginator paginator;

    public PageList() {}
    
	public PageList(Collection<? extends E> c) {
		super(c);
	}

	
	public PageList(Collection<? extends E> c, com.ciicsh.gto.fcsupportcenter.tax.util.page.Paginator p) {
        super(c);
        this.paginator = p;
    }

    public PageList(com.ciicsh.gto.fcsupportcenter.tax.util.page.Paginator p) {
        this.paginator = p;
    }


	/**
	 * 得到分页器，通过Paginator可以得到总页数等值
	 * @return
	 */
	public com.ciicsh.gto.fcsupportcenter.tax.util.page.Paginator getPaginator() {
		return paginator;
	}

	
}
