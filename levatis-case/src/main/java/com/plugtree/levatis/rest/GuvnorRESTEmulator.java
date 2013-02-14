package com.plugtree.levatis.rest;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Content.Type;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.commons.io.IOUtils;

import com.plugtree.levatis.repo.Item;
import com.plugtree.levatis.repo.Repository;

@Path("/packages")
public class GuvnorRESTEmulator {

	public static final QName UUID = new QName("", "uuid");
	public static final QName VALUE = new QName("", "value");
	public static final QName FORMAT = new QName("", "format");
	public static final QName METADATA = new QName("", "metadata");

    @Context
    protected UriInfo uriInfo;
    private Repository repository = Repository.getInstance();

    private HttpHeaders headers;

    @Context
    public void setHttpHeaders(HttpHeaders theHeaders) {
        headers = theHeaders;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Package> getPackagesAsJAXB() {
        List<Package> ret = new ArrayList<Package>();
    	Package pkg = new Package();
    	pkg.setTitle("levatis");
        ret.add(pkg); 
        return ret;
    }
    
    @GET
    @Path("levatis/assets")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Asset> getAssetsAsJAXB(@QueryParam("format") List<String> formats) {
        try {
            List<Asset> ret = new ArrayList<Asset>();
            List<Item> items = repository.loadAll();
            if (items != null) {
            	for (Item item : items) {
            		ret.add(toAsset(item));
            	}
            }
            return ret;
        } catch (RuntimeException e) {
            throw new WebApplicationException(e);
        }
    }

	@GET
    @Path("levatis/assets/{assetName}")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Entry getAssetAsAtom(@PathParam("assetName") String assetName) {
    	String packageName = "levatis";
        if (!assetExists(assetName)) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Asset [" + assetName + "] of package [" + packageName + "] does not exist").build());
        }
        try {
            //Throws RulesRepositoryException if the package or asset does not exist
            Item asset = repository.loadByName(assetName);
            return toAssetEntryAbdera(asset, uriInfo);
        } catch (RuntimeException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("levatis/assets/{assetName}/binary")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getAssetBinary(@PathParam("assetName") String assetName) {
        if (!assetExists(assetName)) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Asset [" + assetName + "] does not exist").build());
        }
        try {
            //Throws RulesRepositoryException if the package or asset does not exist
            Item asset = repository.loadByName(assetName);
            String fileName = asset.getName() + "." + asset.getFormat();
            return Response.ok(asset.getContent()).header("Content-Disposition", "attachment; filename=" + fileName).build();
        } catch (RuntimeException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("levatis/assets/{assetName}/source")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAssetSource(@PathParam("assetName") String assetName) {
        if (!assetExists(assetName)) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Asset [" + assetName + "] does not exist").build());
        }
        try {
            //Throws RulesRepositoryException if the package or asset does not exist
            Item asset = repository.loadByName(assetName);
            return asset.getContentAsString();
        } catch (RuntimeException e) {
            throw new WebApplicationException(e);
        }
    }

    @POST
    @Path("levatis/assets")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Entry createAssetFromBinary(InputStream is) {
        try {
            String assetName = getHttpHeader(headers, "slug");
            if (assetName == null) {
                throw new WebApplicationException(Response.status(500).entity("Slug header is missing").build());
            } else {
                try {
                    assetName = URLDecoder.decode(assetName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException("This server does not support UTF-8 encoding.", e);
                }
            }
            repository.save(assetName, IOUtils.toByteArray(is));
            Item item = repository.loadAll().iterator().next();
            return toAssetEntryAbdera(item, uriInfo);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }

    @DELETE
    @Path("levatis/assets/{assetName}/")
    public void deleteAsset(@PathParam("assetName") String assetName) {
        repository.delete(assetName);
    }
    
    private boolean assetExists(final String assetName) {
        return repository.contains(assetName);
    }

    private Asset toAsset(Item item) {
    	Asset asset = new Asset();
		asset.setBinaryContentAttachmentFileName(item.getName());
		asset.setBinaryLink(uriInfo.getRequestUriBuilder().path("{itemName}/binary").build(item.getName()));
		asset.setDescription(item.getName());
		asset.setFormat(item.getFormat());
		asset.setRefLink(uriInfo.getRequestUriBuilder().path(item.getName()).build());
		asset.setSourceLink(uriInfo.getRequestUriBuilder().path("{itemName}/source").build(item.getName()));
		asset.setTitle(item.getName());
		asset.setUuid(item.getKey());
		return asset;
	}

    public static Entry toAssetEntryAbdera(Item a, UriInfo uriInfo) {
        URI baseURL = uriInfo.getBaseUriBuilder()
                    .path("packages/{packageName}/assets/{assetName}")
                    .build(a.getName(), a.getName());
        Factory factory = Abdera.getNewFactory();
        org.apache.abdera.model.Entry e = factory.getAbdera().newEntry();
        e.setTitle(a.getName());
        e.setSummary(a.getName());
        e.setBaseUri(baseURL.toString());
        e.setId(baseURL.toString());

        //generate meta data
        ExtensibleElement extension = e.addExtension(METADATA);
        ExtensibleElement childExtension = extension.addExtension(UUID);
        childExtension.addSimpleExtension(VALUE, a.getKey());

        childExtension = extension.addExtension(FORMAT);
        childExtension.addSimpleExtension(VALUE, a.getFormat());

        org.apache.abdera.model.Content content = factory.newContent();
        content.setSrc(UriBuilder.fromUri(baseURL).path("binary").build().toString());
        content.setMimeType("application/octet-stream");
        content.setContentType(Type.MEDIA);
        e.setContentElement(content);

        return e;
    }
    
    //HTTP header names are case-insensitive
    private String getHttpHeader(HttpHeaders headers, String headerName) {

        MultivaluedMap<String, String> heads = headers.getRequestHeaders();
        Iterator<String> it = heads.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (headerName.equalsIgnoreCase(key)) {
                List<String> h = heads.get(key);

                if (h != null && h.size() > 0) {
                    return h.get(0);
                }
            }
        }

        return null;
    }


}
