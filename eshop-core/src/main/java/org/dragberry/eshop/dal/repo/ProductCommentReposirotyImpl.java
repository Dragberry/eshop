package org.dragberry.eshop.dal.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.dragberry.eshop.dal.dto.ProductCommentDTO;
import org.dragberry.eshop.dal.entity.Comment.Status;
import org.dragberry.eshop.dal.entity.ProductComment;
import org.springframework.data.domain.Pageable;

/**
 * Implementation of custom comment search
 * @author Maksim Dragun
 */
public class ProductCommentReposirotyImpl implements ProductCommentReposiroty {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<ProductCommentDTO> findProductComments(Pageable page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProductCommentDTO> query = cb.createQuery(ProductCommentDTO.class);
		Root<ProductComment> root = query.from(ProductComment.class);
		Join<?, ?> commentRoot = root.join("comment");
		Join<?, ?> productRoot = root.join("productArticle");
		Join<?, ?> categoryRoot = productRoot.join("category");
		query.multiselect(
				commentRoot.get("entityKey"),
				commentRoot.get("userName"),
				commentRoot.get("text"),
				commentRoot.get("dateTime"),
				root.get("mark"),
				productRoot.get("entityKey"),
				productRoot.get("article"),
				productRoot.get("title"),
				productRoot.get("reference"),
				categoryRoot.get("reference"))
		.where(cb.equal(commentRoot.get("status"), Status.ACTIVE))
		.orderBy(cb.desc(commentRoot.get("dateTime")));
		return em.createQuery(query)
				.setMaxResults(page.getPageSize())
				.setFirstResult(page.getPageNumber()).getResultList();
	}
}
