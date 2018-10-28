package org.dragberry.eshop.dal.repo;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dragberry.eshop.dal.sort.Roots;
import org.dragberry.eshop.dal.sort.SortConfig;
import org.dragberry.eshop.dal.sort.SortContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import lombok.extern.log4j.Log4j;

@Log4j
public abstract class AbstractSearchQuery <T, R extends Roots> {
	
	private static final Pattern SORT_PATTERN = Pattern.compile("^(.*?)\\[(.*?)\\]$");
	
	private static final String SORT_PARAM = "sort";
	
	private static final String FROM_PARAM = "from[{0}]";
	
	private static final String TO_PARAM = "to[{0}]";
	
	private static final String ATTRIBUTE_PARAM = "attribute[{0}]";
	
	protected final EntityManager entityManager;
	protected final CriteriaBuilder cb;
	protected final CriteriaQuery<T> query;
	
	public AbstractSearchQuery(Class<T> resultClass, EntityManager entityManager) {
		this.entityManager = entityManager;
		this.cb = this.entityManager.getCriteriaBuilder();
		this.query = this.cb.createQuery(resultClass);
	}
	
	public Page<T> search(Pageable pageRequest, Map<String, String[]> searchParams) {
		query.distinct(true).multiselect(getSelectionList());
		where(searchParams).ifPresent(query::where);
		orderBy(searchParams).ifPresent(query::orderBy); 
		return new PageImpl<>(entityManager.createQuery(query)
				.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize())
				.setMaxResults(pageRequest.getPageSize())
				.getResultList(), pageRequest, 0);
	}
	
	protected abstract List<Selection<?>> getSelectionList();
	
	protected abstract Optional<Predicate> where(Map<String, String[]> searchParams);
	
	protected Optional<Order> orderBy(Map<String, String[]> searchParams) {
		String[] values = searchParams.get(SORT_PARAM);
		if (ArrayUtils.getLength(values) > 0) {
			Matcher m = SORT_PATTERN.matcher(values[0]);
			if (m.matches()) {
				return getSortConfig().get(m.group(1)).getOrder(getSortContext(), m.group(2));
			}
		}
		return getSortConfig().getDefault().getOrder(getSortContext());
	}
	
	protected abstract SortConfig<R> getSortConfig();
	
	protected abstract SortContext<R> getSortContext();
	
	protected List<Predicate> numericRange(String param, Path<BigDecimal> path, Map<String, String[]> searchParams) {
		List<Predicate> predicates = new ArrayList<>(2);
		String[] from = searchParams.get(MessageFormat.format(FROM_PARAM, param));
		if (ArrayUtils.isNotEmpty(from) && NumberUtils.isCreatable(from[0])) {
			predicates.add(cb.greaterThanOrEqualTo(path, new BigDecimal(from[0])));
		}
		String[] to = searchParams.get(MessageFormat.format(TO_PARAM, param));
		if (ArrayUtils.isNotEmpty(to) && NumberUtils.isCreatable(to[0])) {
			predicates.add(cb.lessThanOrEqualTo(path, new BigDecimal(to[0])));
		}
		return predicates;
	}
	
	protected List<Predicate> dateRange(String param, Path<LocalDateTime> path, Map<String, String[]> searchParams) {
		List<Predicate> predicates = new ArrayList<>(2);
		String[] from = searchParams.get(MessageFormat.format(FROM_PARAM, param));
		if (ArrayUtils.isNotEmpty(from)) {
			try {
				LocalDateTime fromDate = LocalDateTime.parse(from[0]);
				predicates.add(cb.greaterThanOrEqualTo(path, fromDate));
			} catch (DateTimeParseException exc) {
 				log.warn("Unable to parse date " + from[0]);
			}
		}
		String[] to = searchParams.get(MessageFormat.format(TO_PARAM, param));
		if (ArrayUtils.isNotEmpty(to)) {
			try {
				LocalDateTime toDate = LocalDateTime.parse(to[0]);
				predicates.add(cb.lessThanOrEqualTo(path, toDate));
			} catch (DateTimeParseException exc) {
				log.warn("Unable to parse date " + to[0]);
			}
		}
		return predicates;
	}
	
	protected List<Predicate> in(String param, Path<Long> path, Map<String, String[]> searchParams) {
		String[] values = searchParams.get(MessageFormat.format(ATTRIBUTE_PARAM, param));
		if (ArrayUtils.isNotEmpty(values)) {
			return Arrays.asList(path.in(Arrays.stream(values)
					.filter(NumberUtils::isCreatable)
					.map(Long::valueOf)
					.collect(Collectors.toList())));
		}
		return Collections.emptyList();
	}
}