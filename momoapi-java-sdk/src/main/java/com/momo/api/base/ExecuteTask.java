package com.momo.api.base;

import com.momo.api.base.exception.MoMoException;
import com.momo.api.base.exception.UnauthorizedException;

/**
 * 
 * Interface ExecuteTask
 */
public interface ExecuteTask {
    /***
     *
     * @return
     * @throws MoMoException
     * @throws UnauthorizedException
     */
    HttpResponse execute() throws MoMoException, UnauthorizedException;
}
