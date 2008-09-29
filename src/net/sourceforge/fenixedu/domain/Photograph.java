package net.sourceforge.fenixedu.domain;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.joda.time.DateTime;

public class Photograph extends Photograph_Base {

    private static final int COMPRESSED_PHOTO_WIDTH = 100;

    private static final int COMPRESSED_PHOTO_HEIGHT = 100;

    public Photograph() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setSubmission(new DateTime());
	setRejectedAcknowledged(Boolean.FALSE);
    }

    public Photograph(ContentType contentType, ByteArray content, ByteArray compressed, PhotoType photoType) {
	this();
	setContentType(contentType);
	setRawContent(content);
	setContent(compressed);
	setPhotoType(photoType);
    }

    public Photograph(ContentType contentType, ByteArray content, PhotoType photoType) {
	this(contentType, content, compressImage(content.getBytes(), contentType), photoType);
    }

    @Override
    public Person getPerson() {
	if (super.getPerson() != null)
	    return super.getPerson();
	if (hasNext())
	    return getNext().getPerson();
	return null;
    }

    @Override
    public void setPhotoType(PhotoType photoType) {
	super.setPhotoType(photoType);
	if (photoType == PhotoType.INSTITUTIONAL) {
	    setState(PhotoState.APPROVED);
	} else if (photoType == PhotoType.USER) {
	    setState(PhotoState.PENDING);
	} else {
	    setState(PhotoState.PENDING);
	}
    }

    @Override
    public void setState(PhotoState state) {
	super.setState(state);
	setStateChange(new DateTime());
	if (state == PhotoState.PENDING) {
	    setPendingHolder(RootDomainObject.getInstance());
	} else {
	    setPendingHolder(null);
	}
    }

    @Override
    public void setPrevious(Photograph previous) {
	if (previous.getState() == PhotoState.PENDING) {
	    previous.setState(PhotoState.USER_REJECTED);
	    previous.setRejectedAcknowledged(Boolean.TRUE);
	}
	super.setPrevious(previous);
    }

    public byte[] getContents() {
	return this.getContent().getBytes();
    }

    public void delete() {
	removeRootDomainObject();
	if (hasPendingHolder())
	    removePendingHolder();
	removePerson();
	Photograph prev = getPrevious();
	if (prev != null) {
	    removePrevious();
	    prev.delete();
	}
	Photograph next = getNext();
	if (next != null) {
	    removeNext();
	}
	super.deleteDomainObject();
    }

    static private ByteArray compressImage(byte[] content, ContentType contentType) {
	BufferedImage image;
	try {
	    image = ImageIO.read(new ByteArrayInputStream(content));
	} catch (IOException e) {
	    throw new DomainException("photograph.compress.errorReadingImage", e);
	}
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	// calculate resize factor
	double resizeFactor = Math.min((double) COMPRESSED_PHOTO_WIDTH / image.getWidth(), (double) COMPRESSED_PHOTO_HEIGHT
		/ image.getHeight());

	// resize image
	AffineTransform tx = new AffineTransform();
	tx.scale(resizeFactor, resizeFactor);
	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	image = op.filter(image, null);

	// set compression
	ImageWriter writer = ImageIO.getImageWritersByMIMEType(contentType.getMimeType()).next();
	ImageWriteParam param = writer.getDefaultWriteParam();
	if (contentType == ContentType.JPG) {
	    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    param.setCompressionQuality(1);
	}

	// write to stream
	try {
	    writer.setOutput(ImageIO.createImageOutputStream(outputStream));
	    writer.write(null, new IIOImage(image, null, null), param);
	} catch (IOException e) {
	    throw new DomainException("photograph.compress.errorWritingImage", e);
	}
	return new ByteArray(outputStream.toByteArray());
    }
}
