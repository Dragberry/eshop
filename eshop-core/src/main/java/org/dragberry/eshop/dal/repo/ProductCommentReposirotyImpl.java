package org.dragberry.eshop.dal.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.dragberry.eshop.dal.dto.ProductCommentDTO;
import org.dragberry.eshop.dal.entity.Category;
import org.dragberry.eshop.dal.entity.Category_;
import org.dragberry.eshop.dal.entity.Comment;
import org.dragberry.eshop.dal.entity.Comment_;
import org.dragberry.eshop.dal.entity.File;
import org.dragberry.eshop.dal.entity.File_;
import org.dragberry.eshop.dal.entity.Comment.Status;
import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductArticle_;
import org.dragberry.eshop.dal.entity.ProductComment;
import org.dragberry.eshop.dal.entity.ProductComment_;
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
		Join<ProductComment, Comment> commentRoot = root.join(ProductComment_.comment);
		Join<ProductComment, ProductArticle> productRoot = root.join(ProductComment_.productArticle);
		Join<ProductArticle, Category> categoryRoot = productRoot.join(ProductArticle_.category);
		Join<ProductArticle, File> mainImageRoot = productRoot.join(ProductArticle_.mainImage, JoinType.LEFT);
		query.multiselect(
				commentRoot.get(Comment_.entityKey),
				commentRoot.get(Comment_.userName),
				commentRoot.get(Comment_.text),
				commentRoot.get(Comment_.dateTime),
				root.get(ProductComment_.mark),
				productRoot.get(ProductArticle_.entityKey),
				productRoot.get(ProductArticle_.article),
				productRoot.get(ProductArticle_.title),
				productRoot.get(ProductArticle_.reference),
				categoryRoot.get(Category_.reference),
				mainImageRoot.get(File_.path))
		.where(cb.equal(commentRoot.get(Comment_.status), Status.ACTIVE))
		.orderBy(cb.desc(commentRoot.get(Comment_.dateTime)));
		return em.createQuery(query)
				.setMaxResults(page.getPageSize())
				.setFirstResult(page.getPageNumber()).getResultList();
	}
}
