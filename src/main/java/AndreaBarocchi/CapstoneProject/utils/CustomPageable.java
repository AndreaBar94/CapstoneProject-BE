package AndreaBarocchi.CapstoneProject.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPageable implements Pageable {
    private int page;
    private int size;
    private Sort sort;

    public CustomPageable(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return (long) page * (long) size;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new CustomPageable(page + 1, size, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return page == 0 ? this : new CustomPageable(page - 1, size, sort);
    }

    @Override
    public Pageable first() {
        return new CustomPageable(0, size, sort);
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }

	@Override
	public Pageable withPage(int pageNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}
